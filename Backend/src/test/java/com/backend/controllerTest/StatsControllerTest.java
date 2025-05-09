package com.backend.controllerTest;

import com.backend.controller.StatsController;
import com.backend.modal.DashboardStats;
import com.backend.service.StatsService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class StatsControllerTest {

    @InjectMocks
    private StatsController statsController;

    @Mock
    private StatsService statsService;

    private DashboardStats mockStats;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        mockStats = new DashboardStats();
        mockStats.setCustomers(100);
        mockStats.setProducts(50);
        mockStats.setSales(200);
        mockStats.setRevenue(100000.0);
        mockStats.setGrowthRate(15.5);
    }

    @Test
    public void testGetDashboardStats_Success() {
        when(statsService.getDashboardStats()).thenReturn(mockStats);

        ResponseEntity<DashboardStats> response = statsController.getDashboardStats();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockStats, response.getBody());
        verify(statsService, times(1)).getDashboardStats();
    }

    @Test
    public void testGetWeeklyStats_Success() {
        Map<String, Object> weeklyStats = new HashMap<>();
        weeklyStats.put("weeklyProfit", 5000.0);
        weeklyStats.put("newOrdersCount", 40);

        when(statsService.getWeeklyStats()).thenReturn(weeklyStats);

        ResponseEntity<Map<String, Object>> response = statsController.getWeeklyStats();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(weeklyStats, response.getBody());
        verify(statsService, times(1)).getWeeklyStats();
    }

    @Test
    public void testGetYearlyStats_Success() {
        Map<String, Object> yearlyStats = new HashMap<>();
        yearlyStats.put("yearlyRevenue", 1000000.0);
        yearlyStats.put("yearlyOrdersCount", 800);

        when(statsService.getYearlyStats()).thenReturn(yearlyStats);

        ResponseEntity<Map<String, Object>> response = statsController.getYearlyStats();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(yearlyStats, response.getBody());
        verify(statsService, times(1)).getYearlyStats();
    }
    @Test
    public void testGetDashboardStats_NullResponse() {
        when(statsService.getDashboardStats()).thenReturn(null);

        ResponseEntity<DashboardStats> response = statsController.getDashboardStats();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(statsService, times(1)).getDashboardStats();
    }

    @Test
    public void testGetWeeklyStats_EmptyMap() {
        when(statsService.getWeeklyStats()).thenReturn(Collections.emptyMap());

        ResponseEntity<Map<String, Object>> response = statsController.getWeeklyStats();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(statsService, times(1)).getWeeklyStats();
    }

    @Test
    public void testGetYearlyStats_EmptyMap() {
        when(statsService.getYearlyStats()).thenReturn(Collections.emptyMap());

        ResponseEntity<Map<String, Object>> response = statsController.getYearlyStats();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(statsService, times(1)).getYearlyStats();
    }

    @Test
    public void testGetWeeklyStats_AbnormalValues() {
        Map<String, Object> weeklyStats = new HashMap<>();
        weeklyStats.put("weeklyProfit", -9999.0);
        weeklyStats.put("newOrdersCount", -1);

        when(statsService.getWeeklyStats()).thenReturn(weeklyStats);

        ResponseEntity<Map<String, Object>> response = statsController.getWeeklyStats();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(weeklyStats, response.getBody());
        verify(statsService).getWeeklyStats();
    }

    @Test
    public void testGetYearlyStats_AbnormalValues() {
        Map<String, Object> yearlyStats = new HashMap<>();
        yearlyStats.put("yearlyRevenue", -50000.0);
        yearlyStats.put("yearlyOrdersCount", -100);

        when(statsService.getYearlyStats()).thenReturn(yearlyStats);

        ResponseEntity<Map<String, Object>> response = statsController.getYearlyStats();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(yearlyStats, response.getBody());
        verify(statsService).getYearlyStats();
    }
    @Test
    public void testGetDashboardStats_ServiceThrowsException() {
        when(statsService.getDashboardStats()).thenThrow(new RuntimeException("Service failure"));

        assertThrows(RuntimeException.class, () -> {
            statsController.getDashboardStats();
        });

        verify(statsService).getDashboardStats();
    }

}
