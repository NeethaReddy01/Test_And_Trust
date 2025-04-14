package com.backend.controllerTest;

import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.backend.service.OrderService;
import com.backend.modal.Order;
import com.backend.exception.OrderException;
import com.backend.controller.AdminOrderController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;

@ExtendWith(MockitoExtension.class)
public class AdminOrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private AdminOrderController adminOrderController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminOrderController).build();
    }

    @Test
    void testGetAllOrders() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of(new Order()));

        mockMvc.perform(get("/api/admin/orders/"))
               .andExpect(status().isAccepted())
               .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testConfirmedOrder() throws Exception {
        Long orderId = 1L;
        Order order = new Order();
        when(orderService.confirmedOrder(orderId)).thenReturn(order);

        mockMvc.perform(put("/api/admin/orders/{orderId}/confirmed", orderId)
                        .header("Authorization", "Bearer token"))
               .andExpect(status().isAccepted());
    }

    @Test
    void testShippedOrder() throws Exception {
        Long orderId = 1L;
        Order order = new Order();
        when(orderService.shippedOrder(orderId)).thenReturn(order);

        mockMvc.perform(put("/api/admin/orders/{orderId}/ship", orderId)
                        .header("Authorization", "Bearer token"))
               .andExpect(status().isAccepted());
    }

    @Test
    void testDeliveredOrder() throws Exception {
        Long orderId = 1L;
        Order order = new Order();
        when(orderService.deliveredOrder(orderId)).thenReturn(order);

        mockMvc.perform(put("/api/admin/orders/{orderId}/deliver", orderId)
                        .header("Authorization", "Bearer token"))
               .andExpect(status().isAccepted());
    }

    @Test
    void testCanceledOrder() throws Exception {
        Long orderId = 1L;
        Order order = new Order();
        when(orderService.cancledOrder(orderId)).thenReturn(order);

        mockMvc.perform(put("/api/admin/orders/{orderId}/cancel", orderId)
                        .header("Authorization", "Bearer token"))
               .andExpect(status().isAccepted());
    }

    @Test
    void testDeleteOrder() throws Exception {
        Long orderId = 1L;
        doNothing().when(orderService).deleteOrder(orderId);

        mockMvc.perform(delete("/api/admin/orders/{orderId}/delete", orderId)
                        .header("Authorization", "Bearer token"))
               .andExpect(status().isAccepted());
    }
}

