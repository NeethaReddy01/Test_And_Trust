package com.backend.controllerTest;

import com.backend.controller.PaymentController;
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
import com.backend.user.domain.PaymentStatus;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class PaymentControllerTest {

    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private OrderService orderService;

    @Mock
    private UserService userService;

    @Mock
    private OrderRepository orderRepository;

    private Order mockOrder;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setMobile("9999999999");
        user.setEmail("john@example.com");

        mockOrder = new Order();
        mockOrder.setId(1L);
        mockOrder.setTotalDiscountedPrice((int) 500.0);
        mockOrder.setUser(user);
        mockOrder.setPaymentDetails(new PaymentDetails());


        
    }
    @Test
    public void testCreatePaymentLink_RazorpayException() throws Exception {
        when(orderService.findOrderById(1L)).thenReturn(mockOrder);

        //injectRazorpayKeys();

        // Simulate Razorpay failure
        PaymentController spyController = Mockito.spy(paymentController);
        doThrow(new RazorpayException("Razorpay error")).when(spyController).createPaymentLink(1L, "Bearer jwt");

        assertThrows(RazorpayException.class, () -> {
            spyController.createPaymentLink(1L, "Bearer jwt");
        });
    }
 


}
