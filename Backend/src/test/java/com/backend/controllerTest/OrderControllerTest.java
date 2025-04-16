package com.backend.controllerTest;

import com.backend.controller.OrderController;
import com.backend.exception.OrderException;
import com.backend.exception.UserException;
import com.backend.modal.Order;
import com.backend.modal.Address;
import com.backend.modal.User;
import com.backend.service.OrderService;
import com.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

public class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @Mock
    private UserService userService;

    @InjectMocks
    private OrderController orderController;

    private User mockUser;
    private Address mockAddress;
    private Order mockOrder;

    @BeforeEach
    public void setUp() {
       
        MockitoAnnotations.openMocks(this);

       
        mockUser = new User();
        mockUser.setId(1L);

       
        mockAddress = new Address(null, "John", "Doe", "123 Street", "City", "State", "12345", mockUser, "1234567890");

        mockOrder = new Order();
        mockOrder.setId(1L);
        mockOrder.setUser(mockUser);

        
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    public void testCreateOrder() throws Exception {
        when(userService.findUserProfileByJwt(anyString())).thenReturn(mockUser);
        when(orderService.createOrder(any(User.class), any(Address.class))).thenReturn(mockOrder);

        mockMvc.perform(post("/api/orders/")
                .header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"John\", \"lastName\":\"Doe\", \"streetAddress\":\"123 Street\", \"city\":\"City\", \"state\":\"State\", \"zipCode\":\"12345\", \"mobile\":\"1234567890\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.user.id").value(1));
    }

    @Test
    public void testGetUserOrderHistory() throws Exception {
        when(userService.findUserProfileByJwt(anyString())).thenReturn(mockUser);
        when(orderService.usersOrderHistory(mockUser.getId())).thenReturn(List.of(mockOrder));

        mockMvc.perform(get("/api/orders/user")
                .header("Authorization", "Bearer token"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    public void testGetOrderById() throws Exception {
        when(userService.findUserProfileByJwt(anyString())).thenReturn(mockUser);
        when(orderService.findOrderById(1L)).thenReturn(mockOrder);

        mockMvc.perform(get("/api/orders/1")
                .header("Authorization", "Bearer token"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(1));
    }
    
    
   
    @Test
    public void testGetUserOrderHistory_Empty() throws Exception {
        when(userService.findUserProfileByJwt(anyString())).thenReturn(mockUser);
        when(orderService.usersOrderHistory(mockUser.getId())).thenReturn(List.of());

        mockMvc.perform(get("/api/orders/user")
                .header("Authorization", "Bearer token"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("[]")); // Expecting empty array
    }
    
    //..........
    @Test
    public void testCreateOrder_NullAddress() throws Exception {
        when(userService.findUserProfileByJwt(anyString())).thenReturn(mockUser);

        mockMvc.perform(post("/api/orders/")
                .header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetOrderById_InvalidIdFormat() throws Exception {
        mockMvc.perform(get("/api/orders/abc")
                .header("Authorization", "Bearer token"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void testGetUserOrderHistory_MissingAuthHeader() throws Exception {
        mockMvc.perform(get("/api/orders/user"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void testGetOrderById_MissingHeader() throws Exception {
        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isBadRequest());
    }

   
}
