package com.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.backend.modal.DashboardStats;
import com.backend.modal.Order;
import com.backend.modal.Product;
import com.backend.modal.User;
import com.backend.repository.OrderRepository;
import com.backend.repository.ProductRepository;
import com.backend.repository.UserRepository;

@Service
public class StatsServiceImpl implements StatsService {

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;

    public StatsServiceImpl(OrderRepository orderRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public DashboardStats getDashboardStats() {
        DashboardStats stats = new DashboardStats();

        // Count total users
        long totalUsers = userRepository.count();
        stats.setCustomers(totalUsers);

        // Count total products
        long totalProducts = productRepository.count();
        stats.setProducts(totalProducts);

        // Count total orders
        long totalOrders = orderRepository.count();
        stats.setSales(totalOrders);

        // Calculate total revenue
        double totalRevenue = calculateTotalRevenue();
        stats.setRevenue(totalRevenue);

        // Calculate growth rate (comparing current month to previous month)
        double growthRate = calculateGrowthRate();
        stats.setGrowthRate(growthRate);

        return stats;
    }
    
    @Override
    public Map<String, Object> getWeeklyStats() {
        Map<String, Object> weeklyStats = new HashMap<>();
        
        // Get current date and date 7 days ago
        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusDays(7);
        LocalDate twoWeeksAgo = today.minusDays(14);
        LocalDate monthAgo = today.minusDays(30);
        
        // Weekly profit calculation (sum of all orders' total price in the last week)
        List<Order> weeklyOrders = orderRepository.findByOrderDateBetween(weekAgo, today);
        double weeklyProfit = weeklyOrders.stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();
        
        // Previous week for comparison
        List<Order> previousWeekOrders = orderRepository.findByOrderDateBetween(twoWeeksAgo, weekAgo);
        double previousWeekProfit = previousWeekOrders.stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();
        
        // Calculate profit trend
        double profitTrendPercentage = calculatePercentageChange(previousWeekProfit, weeklyProfit);
        
        // Refunds calculation (mocked as we don't have actual refund data)
        // In a real scenario, you would query a refunds table or use a status field in orders
        double refundAmount = 78.0; // Mocked value, replace with actual calculation
        double previousMonthRefunds = 92.0; // Mocked value for comparison
        double refundTrendPercentage = calculatePercentageChange(previousMonthRefunds, refundAmount);
        
        // New orders count (orders created in the last week)
        long newOrdersCount = weeklyOrders.size();
        
        // Previous week orders for comparison
        long previousWeekOrdersCount = previousWeekOrders.size();
        double ordersTrendPercentage = calculatePercentageChange(previousWeekOrdersCount, newOrdersCount);
        
        // Sales queries (mocked as this might come from a customer support system)
        // In a real scenario, this would be fetched from a support ticket system
        long salesQueries = 15; // Mocked value
        long previousWeekSalesQueries = 18; // Mocked value for comparison
        double queriesTrendPercentage = calculatePercentageChange(previousWeekSalesQueries, salesQueries);
        
        // Populate the map with calculated values
        weeklyStats.put("weeklyProfit", weeklyProfit);
        weeklyStats.put("profitTrend", profitTrendPercentage);
        weeklyStats.put("refundAmount", refundAmount);
        weeklyStats.put("refundTrend", refundTrendPercentage);
        weeklyStats.put("newOrdersCount", newOrdersCount);
        weeklyStats.put("ordersTrend", ordersTrendPercentage);
        weeklyStats.put("salesQueries", salesQueries);
        weeklyStats.put("queriesTrend", queriesTrendPercentage);
        
        return weeklyStats;
    }
    
    private double calculatePercentageChange(double oldValue, double newValue) {
        if (oldValue == 0) {
            return newValue > 0 ? 100.0 : 0.0; // If previous value was zero, consider it 100% growth if new value is positive
        }
        return ((newValue - oldValue) / oldValue) * 100.0;
    }

    private double calculateTotalRevenue() {
        // Get all orders
        List<Order> allOrders = orderRepository.findAll();

        // Sum up the total price of all orders
        return allOrders.stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();
    }

    private double calculateGrowthRate() {
        // Get current month and previous month
        YearMonth currentMonth = YearMonth.from(LocalDate.now());
        YearMonth previousMonth = currentMonth.minusMonths(1);

        // Start and end dates for current month
        LocalDate currentMonthStart = currentMonth.atDay(1);
        LocalDate currentMonthEnd = currentMonth.atEndOfMonth();

        // Start and end dates for previous month
        LocalDate previousMonthStart = previousMonth.atDay(1);
        LocalDate previousMonthEnd = previousMonth.atEndOfMonth();

        // Get orders for current month
        List<Order> currentMonthOrders = orderRepository.findByOrderDateBetween(
                currentMonthStart, currentMonthEnd);

        // Get orders for previous month
        List<Order> previousMonthOrders = orderRepository.findByOrderDateBetween(
                previousMonthStart, previousMonthEnd);

        // Calculate revenue for current month
        double currentMonthRevenue = currentMonthOrders.stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();

        // Calculate revenue for previous month
        double previousMonthRevenue = previousMonthOrders.stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();

        // Calculate growth rate
        if (previousMonthRevenue == 0) {
            return 100.0; // If previous month had zero revenue, consider it 100% growth
        }

        return ((currentMonthRevenue - previousMonthRevenue) / previousMonthRevenue) * 100.0;
    }
}