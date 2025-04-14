package com.backend.serviceTest;

import com.backend.modal.OrderItem;
import com.backend.repository.OrderItemRepository;
import com.backend.service.OrderItemServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderItemServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderItemServiceImplementation orderItemService;

    private OrderItem mockOrderItem;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); 

        mockOrderItem = new OrderItem();
        mockOrderItem.setId(1L);
    }

    @Test
    public void testCreateOrderItem() {
       
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(mockOrderItem);

        OrderItem createdOrderItem = orderItemService.createOrderItem(mockOrderItem);

        assertNotNull(createdOrderItem);
        assertEquals(1L, createdOrderItem.getId());
    }
}
