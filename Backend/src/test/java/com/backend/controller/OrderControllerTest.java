package com.backend.controller;

import com.backend.modal.Address;
import com.backend.modal.Order;
import com.backend.modal.User;
import com.backend.service.OrderService;
import com.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false) // disables Spring Security filters
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserService userService;

    private User user;
    private Order order;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        order = new Order();
        order.setId(100L);
    }

    @Test
    void testCreateOrderHandler() throws Exception {
        Address address = new Address();
        address.setCity("Hyderabad");

        Mockito.when(userService.findUserProfileByJwt("Bearer token")).thenReturn(user);
        Mockito.when(orderService.createOrder(Mockito.any(User.class), Mockito.any(Address.class))).thenReturn(order);

        mockMvc.perform(post("/api/orders/")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer token")
                .content(new ObjectMapper().writeValueAsString(address)))
                .andExpect(status().isOk());
    }

    @Test
    void testUsersOrderHistoryHandler() throws Exception {
        List<Order> orders = Arrays.asList(order);

        Mockito.when(userService.findUserProfileByJwt("Bearer token")).thenReturn(user);
        Mockito.when(orderService.usersOrderHistory(user.getId())).thenReturn(orders);

        mockMvc.perform(get("/api/orders/user")
                .header("Authorization", "Bearer token"))
                .andExpect(status().isAccepted());
    }

    @Test
    void testFindOrderHandler() throws Exception {
        Mockito.when(userService.findUserProfileByJwt("Bearer token")).thenReturn(user);
        Mockito.when(orderService.findOrderById(100L)).thenReturn(order);

        mockMvc.perform(get("/api/orders/100")
                .header("Authorization", "Bearer token"))
                .andExpect(status().isAccepted());
    }
}
