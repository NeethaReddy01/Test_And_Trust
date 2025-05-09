package com.backend.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.backend.exception.OrderException;
import com.backend.modal.Address;
import com.backend.modal.Cart;
import com.backend.modal.CartItem;
import com.backend.modal.Order;
import com.backend.modal.OrderItem;
import com.backend.modal.PaymentDetails;
import com.backend.modal.Product;
import com.backend.modal.User;
import com.backend.repository.AddressRepository;
import com.backend.repository.OrderItemRepository;
import com.backend.repository.OrderRepository;
import com.backend.repository.UserRepository;
import com.backend.service.CartService;
import com.backend.service.OrderItemService;
import com.backend.service.OrderServiceImplementation;
import com.backend.user.domain.OrderStatus;
import com.backend.user.domain.PaymentStatus;

public class OrderServiceImplementationTest {

    @Mock private OrderRepository orderRepository;
    @Mock private CartService cartService;
    @Mock private AddressRepository addressRepository;
    @Mock private UserRepository userRepository;
    @Mock private OrderItemService orderItemService;
    @Mock private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderServiceImplementation orderService;

    private User user;
    private Address address;
    private Cart cart;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setAddresses(new ArrayList<>());

        address = new Address();
        address.setCity("City");

        cartItem = new CartItem();
        cartItem.setPrice(100);
        cartItem.setQuantity(2);
        cartItem.setSize("M");
        cartItem.setUserId(1L);
        cartItem.setDiscountedPrice(80);
        cartItem.setProduct(new Product());

        cart = new Cart();
        //cart.setCartItems(List.of(cartItem));
        cart.setTotalPrice(200);
        cart.setTotalDiscountedPrice(160);
        cart.setDiscounte(40);
        cart.setTotalItem(2);
    }

    @Test
    void testCreateOrder_Success() {
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(cartService.findUserCart(1L)).thenReturn(cart);
        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(i -> i.getArgument(0));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        Order order = orderService.createOrder(user, address);

        assertNotNull(order);
        assertEquals(OrderStatus.PENDING, order.getOrderStatus());
        assertEquals(PaymentStatus.PENDING, order.getPaymentDetails().getStatus());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testFindOrderById_Success() throws OrderException {
        Order order = new Order();
        order.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order found = orderService.findOrderById(1L);
        assertEquals(1L, found.getId());
    }

    @Test
    void testPlacedOrder_Success() throws OrderException {
        Order order = new Order();
        order.setId(1L);
        order.setPaymentDetails(new PaymentDetails());
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order placed = orderService.placedOrder(1L);
        assertEquals(OrderStatus.PLACED, placed.getOrderStatus());
        assertEquals(PaymentStatus.COMPLETED, placed.getPaymentDetails().getStatus());
    }

    @Test
    void testConfirmedOrder_Success() throws OrderException {
        Order order = new Order();
        order.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        Order confirmed = orderService.confirmedOrder(1L);
        assertEquals(OrderStatus.CONFIRMED, confirmed.getOrderStatus());
    }

    @Test
    void testDeleteOrder_Success() throws OrderException {
        Order order = new Order();
        order.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.deleteOrder(1L);
        verify(orderRepository).deleteById(1L);
    }

    @Test
    void testUsersOrderHistory_Success() {
        List<Order> orders = List.of(new Order());
        when(orderRepository.getUsersOrders(1L)).thenReturn(orders);

        List<Order> result = orderService.usersOrderHistory(1L);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllOrders_Success() {
        List<Order> orders = List.of(new Order());
        when(orderRepository.findAllByOrderByCreatedAtDesc()).thenReturn(orders);

        List<Order> result = orderService.getAllOrders();
        assertEquals(1, result.size());
    }

    @Test
    void testShippedOrder_Success() throws OrderException {
        Order order = new Order();
        order.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        Order shipped = orderService.shippedOrder(1L);
        assertEquals(OrderStatus.SHIPPED, shipped.getOrderStatus());
    }

    @Test
    void testDeliveredOrder_Success() throws OrderException {
        Order order = new Order();
        order.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        Order delivered = orderService.deliveredOrder(1L);
        assertEquals(OrderStatus.DELIVERED, delivered.getOrderStatus());
    }

    @Test
    void testCancledOrder_Success() throws OrderException {
        Order order = new Order();
        order.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        Order canceled = orderService.cancledOrder(1L);
        assertEquals(OrderStatus.CANCELLED, canceled.getOrderStatus());
    }
}
