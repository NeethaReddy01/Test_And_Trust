package com.backend.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backend.modal.DashboardStats;
import com.backend.modal.Order;
import com.backend.repository.OrderRepository;
import com.backend.repository.ProductRepository;
import com.backend.repository.UserRepository;
import com.backend.service.StatsServiceImpl;

@ExtendWith(MockitoExtension.class)
public class StatsServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    private StatsServiceImpl statsService;

    @BeforeEach
    public void setUp() {
        statsService = new StatsServiceImpl(orderRepository, userRepository, productRepository);
    }


    @Test
    public void testGetWeeklyStats() {
        // Mock current date
        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusDays(7);
        LocalDate twoWeeksAgo = today.minusDays(14);

        // Mock repository methods
        when(orderRepository.findByOrderDateBetween(weekAgo, today)).thenReturn(Arrays.asList(
            new Order(),
            new Order()
        ));
        when(orderRepository.findByOrderDateBetween(twoWeeksAgo, weekAgo)).thenReturn(Arrays.asList(
            new Order(),
            new Order()
        ));

        Map<String, Object> weeklyStats = statsService.getWeeklyStats();

        assertNotNull(weeklyStats);
        assertTrue(weeklyStats.containsKey("weeklyProfit"));
        assertTrue(weeklyStats.containsKey("profitTrend"));
        assertTrue(weeklyStats.containsKey("refundAmount"));
        assertTrue(weeklyStats.containsKey("newOrdersCount"));
        assertTrue(weeklyStats.containsKey("salesQueries"));
    }

   

}
