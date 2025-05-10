package com.backend.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.backend.exception.OrderException;
import com.backend.modal.Address;
import com.backend.modal.Order;
import com.backend.modal.User;
import com.backend.repository.OrderRepository;
import com.backend.service.OrderServiceImplementation;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImplementation orderService;

    private User user;
    private Address address;
    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        address = new Address();
        address.setCity("City");

        order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setShippingAddress(address);
        order.setOrderDate(LocalDate.now());
    }

 

    @Test
    void testFindOrderById_Success() throws OrderException {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order foundOrder = orderService.findOrderById(1L);
        assertEquals(order.getId(), foundOrder.getId());
    }

    @Test
    void testUsersOrderHistory_Success() {
        List<Order> orderList = List.of(order);
        when(orderRepository.getUsersOrders(1L)).thenReturn(orderList);

        List<Order> result = orderService.usersOrderHistory(1L);
        assertEquals(1, result.size());
    }


 
}
