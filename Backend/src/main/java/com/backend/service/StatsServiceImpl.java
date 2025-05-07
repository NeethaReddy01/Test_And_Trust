package com.backend.service;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.backend.modal.DashboardStats;
import com.backend.modal.Order;
import com.backend.modal.OrderItem;
import com.backend.modal.Product;
import com.backend.repository.OrderRepository;
import com.backend.repository.ProductRepository;
import com.backend.repository.UserRepository;
import com.backend.user.domain.OrderStatus;

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
    
    @Override
    public Map<String, Object> getYearlyStats() {
        Map<String, Object> yearlyStats = new HashMap<>();
        
        // Get current year and previous year
        int currentYear = Year.now().getValue();
        int previousYear = currentYear - 1;
        
        // Define date ranges for current and previous year
        LocalDate currentYearStart = LocalDate.of(currentYear, 1, 1);
        LocalDate currentYearEnd = LocalDate.of(currentYear, 12, 31);
        LocalDate previousYearStart = LocalDate.of(previousYear, 1, 1);
        LocalDate previousYearEnd = LocalDate.of(previousYear, 12, 31);
        
        // Get orders for current and previous year
        List<Order> currentYearOrders = orderRepository.findByOrderDateBetween(currentYearStart, currentYearEnd);
        List<Order> previousYearOrders = orderRepository.findByOrderDateBetween(previousYearStart, previousYearEnd);
        
        // Calculate yearly revenue
        double yearlyRevenue = currentYearOrders.stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();
                
        // Calculate previous year revenue
        double previousYearRevenue = previousYearOrders.stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();
                
        // Calculate revenue trend percentage
        double revenueTrendPercentage = calculatePercentageChange(previousYearRevenue, yearlyRevenue);
        
        // Calculate total orders for current year
        long yearlyOrdersCount = currentYearOrders.size();
        
        // Calculate total orders for previous year
        long previousYearOrdersCount = previousYearOrders.size();
        
        // Calculate orders trend percentage
        double ordersTrendPercentage = calculatePercentageChange(previousYearOrdersCount, yearlyOrdersCount);
        
        // Calculate average order value for current year
        double avgOrderValue = yearlyOrdersCount > 0 ? yearlyRevenue / yearlyOrdersCount : 0;
        
        // Calculate average order value for previous year
        double previousAvgOrderValue = previousYearOrdersCount > 0 ? previousYearRevenue / previousYearOrdersCount : 0;
        
        // Calculate average order value trend
        double avgOrderValueTrend = calculatePercentageChange(previousAvgOrderValue, avgOrderValue);
        
        // Calculate refunds for current year (based on order status or a hypothetical field)
        // In a real implementation, you would query a refunds table or use a specific status
        double yearlyRefunds = calculateYearlyRefunds(currentYearOrders);
        double previousYearRefunds = calculateYearlyRefunds(previousYearOrders);
        double refundsTrendPercentage = calculatePercentageChange(previousYearRefunds, yearlyRefunds);
        
        // Calculate new customers (users who placed their first order this year)
        long newCustomersCount = calculateNewCustomers(currentYear);
        long previousYearNewCustomers = calculateNewCustomers(previousYear);
        double newCustomersTrend = calculatePercentageChange(previousYearNewCustomers, newCustomersCount);
        
        // Calculate top selling products
        List<Map<String, Object>> topSellingProducts = getTopSellingProductsOfYear(currentYearOrders);
        
        // Calculate quarterly revenue breakdown
        Map<String, Double> quarterlyRevenue = calculateQuarterlyRevenue(currentYearOrders);
        
        // Calculate yearly growth projections (this would be more complex in a real system,
        // potentially involving time series analysis or regression)
        double growthProjection = revenueTrendPercentage > 0 ? revenueTrendPercentage * 1.1 : revenueTrendPercentage * 0.5;
        
        // Populate yearly stats map
        yearlyStats.put("year", currentYear);
        yearlyStats.put("yearlyRevenue", yearlyRevenue);
        yearlyStats.put("revenueTrend", revenueTrendPercentage);
        yearlyStats.put("yearlyOrdersCount", yearlyOrdersCount);
        yearlyStats.put("ordersTrend", ordersTrendPercentage);
        yearlyStats.put("averageOrderValue", avgOrderValue);
        yearlyStats.put("avgOrderValueTrend", avgOrderValueTrend);
        yearlyStats.put("yearlyRefunds", yearlyRefunds);
        yearlyStats.put("refundsTrend", refundsTrendPercentage);
        yearlyStats.put("newCustomersCount", newCustomersCount);
        yearlyStats.put("newCustomersTrend", newCustomersTrend);
        yearlyStats.put("topSellingProducts", topSellingProducts);
        yearlyStats.put("quarterlyRevenue", quarterlyRevenue);
        yearlyStats.put("growthProjection", growthProjection);
        
        return yearlyStats;
    }
    
    /**
     * Calculate the amount of refunds for a given list of orders
     * In a real implementation, this would use actual refund data
     */
    private double calculateYearlyRefunds(List<Order> orders) {
        // Mock implementation - in a real system, this would query refund records
        // or calculate based on orders with a refunded status
        return orders.stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.CANCELLED) // Assuming CANCELLED status is 0
                .mapToDouble(Order::getTotalPrice)
                .sum() * 0.8; // Assuming 80% of cancelled orders result in refunds
    }
    
    /**
     * Calculate the number of new customers who made their first purchase in the given year
     */
    private long calculateNewCustomers(int year) {
        LocalDate yearStart = LocalDate.of(year, 1, 1);
        LocalDate yearEnd = LocalDate.of(year, 12, 31);
        
        // Get all orders for the year
        List<Order> yearOrders = orderRepository.findByOrderDateBetween(yearStart, yearEnd);
        
        // Extract unique user IDs from these orders
        List<Long> userIds = yearOrders.stream()
                .map(order -> order.getUser().getId())
                .distinct()
                .collect(Collectors.toList());
        
        // For each user, check if their first order was in this year
        long newCustomersCount = 0;
        for (Long userId : userIds) {
            List<Order> userOrders = orderRepository.findByUserId(userId);
            if (!userOrders.isEmpty()) {
                // Sort orders by date
                Order earliestOrder = userOrders.stream()
                        .min((o1, o2) -> o1.getOrderDate().compareTo(o2.getOrderDate()))
                        .orElse(null);
                
                if (earliestOrder != null && 
                    earliestOrder.getOrderDate().getYear() == year) {
                    newCustomersCount++;
                }
            }
        }
        
        return newCustomersCount;
    }
    
    /**
     * Get the top selling products for a given year based on order items
     */
    private List<Map<String, Object>> getTopSellingProductsOfYear(List<Order> yearOrders) {
        // In a real implementation, this would be more sophisticated,
        // potentially using a database query with grouping and counting
        
        // Create a map to count product occurrences
        Map<Long, Integer> productSalesCount = new HashMap<>();
        Map<Long, Double> productSalesRevenue = new HashMap<>();
        
        // Count occurrences of each product in order items
        for (Order order : yearOrders) {
            for (OrderItem item : order.getOrderItems()) {
                Long productId = item.getProduct().getId();
                int quantity = item.getQuantity();
                double price = item.getPrice() * quantity;
                
                productSalesCount.put(productId, productSalesCount.getOrDefault(productId, 0) + quantity);
                productSalesRevenue.put(productId, productSalesRevenue.getOrDefault(productId, 0.0) + price);
            }
        }
        
        // Convert to a list of products with their sales counts
        List<Map<String, Object>> topProducts = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : productSalesCount.entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();
            Double revenue = productSalesRevenue.get(productId);
            
            // Get product details
            Product product = productRepository.findById(productId).orElse(null);
            if (product != null) {
                Map<String, Object> productMap = new HashMap<>();
                productMap.put("image", product.getImageUrl());
                productMap.put("id", productId);
                productMap.put("title", product.getTitle());
                productMap.put("brand", product.getBrand());
                productMap.put("category", product.getCategory().getName());
                productMap.put("quantitySold", quantity);
                productMap.put("revenue", revenue);
                
                topProducts.add(productMap);
            }
        }
        
        // Sort by quantity sold (descending)
        topProducts.sort((p1, p2) -> ((Integer) p2.get("quantitySold")).compareTo((Integer) p1.get("quantitySold")));
        
        // Return top 10 or fewer if less than 10 products exist
        return topProducts.stream().limit(10).collect(Collectors.toList());
    }
    
    /**
     * Calculate revenue breakdown by quarters for a given year
     */
    private Map<String, Double> calculateQuarterlyRevenue(List<Order> yearOrders) {
        Map<String, Double> quarterlyRevenue = new HashMap<>();
        quarterlyRevenue.put("Q1", 0.0);
        quarterlyRevenue.put("Q2", 0.0);
        quarterlyRevenue.put("Q3", 0.0);
        quarterlyRevenue.put("Q4", 0.0);
        
        for (Order order : yearOrders) {
            int month = order.getOrderDate().getMonthValue();
            String quarter;
            
            if (month >= 1 && month <= 3) {
                quarter = "Q1";
            } else if (month >= 4 && month <= 6) {
                quarter = "Q2";
            } else if (month >= 7 && month <= 9) {
                quarter = "Q3";
            } else {
                quarter = "Q4";
            }
            
            double currentValue = quarterlyRevenue.get(quarter);
            quarterlyRevenue.put(quarter, currentValue + order.getTotalPrice());
        }
        
        return quarterlyRevenue;
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