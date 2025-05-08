package com.backend.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.backend.modal.DashboardStats;
import com.backend.service.StatsService;
import com.backend.service.StatsServiceImpl; // Correct class import

public class StatsServiceTest {

    @Mock
    private StatsService statsService;  // Mock the StatsService interface

    @InjectMocks
    private StatsServiceImpl statsServiceImpl;  // Inject the StatsServiceImpl

    private DashboardStats dashboardStats;

    @BeforeEach
    void setUp() {
        // Open Mockito mocks and initialize the test data
        MockitoAnnotations.openMocks(this);

        // Initialize a mock DashboardStats
        dashboardStats = new DashboardStats();
       // dashboardStats.setTotalOrders(100);
        dashboardStats.setRevenue(5000);
       // dashboardStats.setTotalUsers(150);
    }

    @Test
    void testGetDashboardStats() {
        // Mock the behavior of statsService
        when(statsService.getDashboardStats()).thenReturn(dashboardStats);

        // Call the method from StatsServiceImpl (not the mock)
        DashboardStats stats = statsServiceImpl.getDashboardStats();
        
        // Assertions to check the expected values
        assertNotNull(stats);
       // assertEquals(100, stats.getTotalOrders());
       // assertEquals(5000, stats.getTotalRevenue());
      //  assertEquals(150, stats.getTotalUsers());

        // Verify that the method was called on the mock
        verify(statsService).getDashboardStats();
    }

    @Test
    void testGetWeeklyStats() {
        // Mock the weekly starts map
        Map<String, Object> weeklyStats = Map.of("totalOrders", 10, "totalRevenue", 500);
        when(statsService.getWeeklyStats()).thenReturn(weeklyStats);

        // Call the method from StatsServiceImpl (not the mock)
        Map<String, Object> stats = statsServiceImpl.getWeeklyStats();

        // Assertions to check the expected values
        assertNotNull(stats);
        assertEquals(10, stats.get("totalOrders"));
        assertEquals(500, stats.get("totalRevenue"));

        // Verify that the method was called on the mock
        verify(statsService).getWeeklyStats();
    }

    @Test
    void testGetYearlyStats() {
        // Mock the yearly starts map
        Map<String, Object> yearlyStats = Map.of("totalOrders", 120, "totalRevenue", 6000);
        when(statsService.getYearlyStats()).thenReturn(yearlyStats);

        // Call the method from StatsServiceImpl (not the mock)
        Map<String, Object> stats = statsServiceImpl.getYearlyStats();

        // Assertions to check the expected values
        assertNotNull(stats);
        assertEquals(120, stats.get("totalOrders"));
        assertEquals(6000, stats.get("totalRevenue"));

        // Verify that the method was called on the mock
        verify(statsService).getYearlyStats();
    }
}
