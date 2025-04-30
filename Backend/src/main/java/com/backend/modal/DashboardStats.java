package com.backend.modal;


import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {
    private long sales;        // Total number of orders
    private long customers;    // Total number of registered users
    private long products;     // Total number of products available
    private double revenue;    // Total revenue from all orders
    private double growthRate; // Monthly growth rate (can be calculated based on previous month)
}
