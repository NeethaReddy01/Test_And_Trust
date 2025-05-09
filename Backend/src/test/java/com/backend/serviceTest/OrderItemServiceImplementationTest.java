package com.backend.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import com.backend.modal.OrderItem;
import com.backend.repository.OrderItemRepository;
import com.backend.service.OrderItemServiceImplementation;

public class OrderItemServiceImplementationTest {

    @Mock private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderItemServiceImplementation orderItemService;

    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setQuantity(2);
        orderItem.setSize("M");
        orderItem.setPrice(100);
        orderItem.setDiscountedPrice(80);
        orderItem.setUserId(1L);
    }

    @Test
    void testCreateOrderItem_Success() {
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        OrderItem savedOrderItem = orderItemService.createOrderItem(orderItem);

        assertNotNull(savedOrderItem);
        assertEquals(1L, savedOrderItem.getId());
        assertEquals(2, savedOrderItem.getQuantity());
        assertEquals("M", savedOrderItem.getSize());
        assertEquals(100, savedOrderItem.getPrice());
        assertEquals(80, savedOrderItem.getDiscountedPrice());
        assertEquals(1L, savedOrderItem.getUserId());

        verify(orderItemRepository).save(orderItem);
    }
}
