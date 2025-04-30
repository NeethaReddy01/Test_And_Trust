package com.backend.service;

import java.util.Map;

import com.backend.modal.DashboardStats;

public interface StatsService {
    DashboardStats getDashboardStats();
    Map<String, Object> getWeeklyStats();
}