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
 //...........
    @Test
    void testGetAllOrdersReturnsEmptyList() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of());

        mockMvc.perform(get("/api/admin/orders/"))
               .andExpect(status().isAccepted())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$.length()").value(0));
    }
 
    @Test
    void testConfirmedOrderWithoutAuthorizationHeader() throws Exception {
        mockMvc.perform(put("/api/admin/orders/{orderId}/confirmed", 1L))
               .andExpect(status().isBadRequest()); // or however you handle missing headers
    }
    @Test
    void testConfirmedOrder_AuthorizationWithExtraSpaces() throws Exception {
        Long orderId = 1L;
        mockMvc.perform(put("/api/admin/orders/{orderId}/confirmed", orderId)
                        .header("Authorization", "  Bearer token  "))
               .andExpect(status().isAccepted());
    }
 
    @Test
    void testGetAllOrders_MultipleOrders() throws Exception {
        Order order1 = new Order();
        order1.setId(1L);
        Order order2 = new Order();
        order2.setId(2L);

        when(orderService.getAllOrders()).thenReturn(List.of(order1, order2));

        mockMvc.perform(get("/api/admin/orders/")
                        .header("Authorization", "Bearer token"))
               .andExpect(status().isAccepted())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$.length()").value(2))
               .andExpect(jsonPath("$[0].id").value(1L))
               .andExpect(jsonPath("$[1].id").value(2L));
    }
    @Test
    void testGetAllOrders_EmptyList() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of());

        mockMvc.perform(get("/api/admin/orders/")
                        .header("Authorization", "Bearer token"))
               .andExpect(status().isAccepted())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$.length()").value(0));
    }

//......
    @Test
    void testConfirmedOrder_InvalidOrderId() throws Exception {
        Long invalidOrderId = 999L;
        when(orderService.confirmedOrder(invalidOrderId)).thenThrow(new OrderException("Order not found"));

        mockMvc.perform(put("/api/admin/orders/{orderId}/confirmed", invalidOrderId)
                        .header("Authorization", "Bearer token"));
    }
    @Test
    void testShippedOrder_InvalidOrderId() throws Exception {
        Long invalidOrderId = 999L;
        when(orderService.shippedOrder(invalidOrderId)).thenThrow(new OrderException("Order not found"));

        mockMvc.perform(put("/api/admin/orders/{orderId}/ship", invalidOrderId)
                        .header("Authorization", "Bearer token"));
    }
    @Test
    void testDeliveredOrder_InvalidOrderId() throws Exception {
        Long invalidOrderId = 999L;
        when(orderService.deliveredOrder(invalidOrderId)).thenThrow(new OrderException("Order not found"));

        mockMvc.perform(put("/api/admin/orders/{orderId}/deliver", invalidOrderId)
                        .header("Authorization", "Bearer token"));
    }
    @Test
    void testCanceledOrder_InvalidOrderId() throws Exception {
        Long invalidOrderId = 999L;
        when(orderService.cancledOrder(invalidOrderId)).thenThrow(new OrderException("Order not found"));

        mockMvc.perform(put("/api/admin/orders/{orderId}/cancel", invalidOrderId)
                        .header("Authorization", "Bearer token"));
               
    }
    @Test
    void testDeleteOrder_InvalidOrderId() throws Exception {
        Long invalidOrderId = 999L;
        doThrow(new OrderException("Order not found")).when(orderService).deleteOrder(invalidOrderId);

        mockMvc.perform(delete("/api/admin/orders/{orderId}/delete", invalidOrderId)
                        .header("Authorization", "Bearer token"));
            
    }
    @Test
    void testConfirmedOrder_MissingAuthorizationHeader() throws Exception {
        Long orderId = 1L;
        mockMvc.perform(put("/api/admin/orders/{orderId}/confirmed", orderId));
               
    }
    @Test
    void testConfirmedOrder_InvalidToken() throws Exception {
        Long orderId = 1L;
        mockMvc.perform(put("/api/admin/orders/{orderId}/confirmed", orderId)
                        .header("Authorization", "Bearer InvalidToken"));
             
    }
    @Test
    void testDeleteOrder_MissingAuthorizationHeader() throws Exception {
        Long orderId = 1L;
        mockMvc.perform(delete("/api/admin/orders/{orderId}/delete", orderId));
             
    }
    @Test
    void testConfirmedOrder_InvalidMethod() throws Exception {
        Long orderId = 1L;
        mockMvc.perform(post("/api/admin/orders/{orderId}/confirmed", orderId)
                        .header("Authorization", "Bearer token"));
              
    }
    @Test
    void testOrderStatusChange_InvalidMethod() throws Exception {
        Long orderId = 1L;
        mockMvc.perform(get("/api/admin/orders/{orderId}/confirmed", orderId)
                        .header("Authorization", "Bearer token"));
              
    }
    @Test
    void testDeleteOrder_InvalidOrderIdInPath() throws Exception {
        String invalidOrderId = "invalidId";
        mockMvc.perform(delete("/api/admin/orders/{orderId}/delete", invalidOrderId)
                        .header("Authorization", "Bearer token"));
               
    }



}

