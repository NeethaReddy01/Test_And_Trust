package com.backend.controller;

import com.backend.exception.OrderException;
import com.backend.exception.UserException;
import com.backend.modal.Order;
import com.backend.modal.PaymentDetails;
import com.backend.modal.User;
import com.backend.repository.OrderRepository;
import com.backend.response.ApiResponse;
import com.backend.response.PaymentLinkResponse;
import com.backend.service.OrderService;
import com.backend.service.UserService;
import com.backend.user.domain.OrderStatus;
import com.backend.user.domain.PaymentStatus;
import com.razorpay.Payment;
import com.razorpay.PaymentClient;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PaymentControllerTest {

    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private OrderService orderService;

    @Mock
    private UserService userService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RazorpayClient razorpayClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePaymentLink() throws RazorpayException, UserException, OrderException {
        // Arrange
        Long orderId = 1L;
        String jwt = "some-jwt-token";

        Order order = new Order();
        order.setId(orderId);
        order.setTotalDiscountedPrice(100);
        order.setUser(new User());

        PaymentLink paymentLink = mock(PaymentLink.class);
        when(paymentLink.get("id")).thenReturn("link-id");
        when(paymentLink.get("short_url")).thenReturn("http://payment-link.com");

        when(orderService.findOrderById(orderId)).thenReturn(order);

        var paymentLinkClient = mock(com.razorpay.PaymentLinkClient.class);
        when(paymentLinkClient.create(any())).thenReturn(paymentLink);

        try {
            var field = RazorpayClient.class.getDeclaredField("paymentLink");
            field.setAccessible(true);
            field.set(razorpayClient, paymentLinkClient);
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }

        // Act
        ResponseEntity<PaymentLinkResponse> response = paymentController.createPaymentLink(orderId, jwt);

        // Assert
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("http://payment-link.com", response.getBody().getPayment_link_url());
    }

    @Test
    void testRedirectPaymentSuccess() throws RazorpayException, OrderException {
        // Arrange
        String paymentId = "payment-id";
        Long orderId = 1L;

        Order order = new Order();
        order.setId(orderId);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentDetails(new PaymentDetails());

        Payment payment = mock(Payment.class);
        when(payment.get("status")).thenReturn("captured");

        when(orderService.findOrderById(orderId)).thenReturn(order);

        PaymentClient paymentClient = mock(PaymentClient.class);
        when(paymentClient.fetch(paymentId)).thenReturn(payment);

        try {
            var field = RazorpayClient.class.getDeclaredField("payments");
            field.setAccessible(true);
            field.set(razorpayClient, paymentClient);
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }

        // Act
        ResponseEntity<ApiResponse> response = paymentController.redirect(paymentId, orderId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("your order get placed", response.getBody().getMessage());
        assertEquals(OrderStatus.PLACED, order.getOrderStatus());
        assertEquals(PaymentStatus.COMPLETED, order.getPaymentDetails().getStatus());
    }

    @Test
    void testRedirectPaymentFailure() throws RazorpayException, OrderException {
        // Arrange
        String paymentId = "payment-id";
        Long orderId = 1L;

        Order order = new Order();
        order.setId(orderId);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentDetails(new PaymentDetails());

        when(orderService.findOrderById(orderId)).thenReturn(order);

        PaymentClient paymentClient = mock(PaymentClient.class);
        when(paymentClient.fetch(paymentId)).thenThrow(new RazorpayException("Payment not found"));

        try {
            var field = RazorpayClient.class.getDeclaredField("payments");
            field.setAccessible(true);
            field.set(razorpayClient, paymentClient);
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }

        // Act & Assert
        RazorpayException thrown = assertThrows(RazorpayException.class, () -> {
            paymentController.redirect(paymentId, orderId);
        });
        assertEquals("Payment not found", thrown.getMessage());
    }
}
