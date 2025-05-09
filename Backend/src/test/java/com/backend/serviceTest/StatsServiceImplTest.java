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
    public void testGetDashboardStats() {
        // Mock repository methods
        when(userRepository.count()).thenReturn(100L);
        when(productRepository.count()).thenReturn(50L);
        when(orderRepository.count()).thenReturn(200L);
        when(orderRepository.findAll()).thenReturn(Arrays.asList(
            new Order(),
            new Order()
        ));

        DashboardStats stats = statsService.getDashboardStats();

        assertNotNull(stats);
        assertEquals(100, stats.getCustomers());
        assertEquals(50, stats.getProducts());
        assertEquals(200, stats.getSales());
        assertEquals(300.0, stats.getRevenue(), 0.001);
        // Verify that growth rate is called (mock or verify if needed)
        // assertTrue(stats.getGrowthRate() >= 0); // Example condition
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

    @Test
    public void testGetYearlyStats() {
        // Mock current year and previous year orders
        int currentYear = LocalDate.now().getYear();
        LocalDate startOfYear = LocalDate.of(currentYear, 1, 1);
        LocalDate endOfYear = LocalDate.of(currentYear, 12, 31);

        // Mock repository methods
        when(orderRepository.findByOrderDateBetween(startOfYear, endOfYear)).thenReturn(Arrays.asList(
            new Order(),
            new Order()
        ));

        Map<String, Object> yearlyStats = statsService.getYearlyStats();

        assertNotNull(yearlyStats);
        assertTrue(yearlyStats.containsKey("year"));
        assertTrue(yearlyStats.containsKey("yearlyRevenue"));
        assertTrue(yearlyStats.containsKey("ordersTrend"));
        assertTrue(yearlyStats.containsKey("newCustomersCount"));
        assertTrue(yearlyStats.containsKey("topSellingProducts"));
    }

    @Test
    public void testCalculatePercentageChange() {
      Double change = null;
		//  double change = statsService.calculatePercentageChange(100.0, 200.0);
        assertEquals(100.0, change);

     //   change = statsService.calculatePercentageChange(0.0, 100.0);
        assertEquals(100.0, change);  // Assuming 100% growth when previous value is 0

       // change = statsService.calculatePercentageChange(200.0, 150.0);
        assertEquals(-25.0, change);
    }

   
    

    @Test
    public void testCalculateGrowthRate() {
        LocalDate currentMonthStart = LocalDate.now().withDayOfMonth(1);
        LocalDate currentMonthEnd = LocalDate.now().withDayOfMonth(30);
        LocalDate previousMonthStart = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate previousMonthEnd = LocalDate.now().minusMonths(1).withDayOfMonth(30);

        when(orderRepository.findByOrderDateBetween(currentMonthStart, currentMonthEnd)).thenReturn(Arrays.asList(
            new Order()
        ));
        when(orderRepository.findByOrderDateBetween(previousMonthStart, previousMonthEnd)).thenReturn(Arrays.asList(
            new Order()
        ));

       int growthRate = 0;
		// double growthRate = statsService.calculateGrowthRate();
        assertTrue(growthRate > 0);  // Growth rate should be positive if current month revenue > previous month
    }

}
