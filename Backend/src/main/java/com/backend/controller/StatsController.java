package com.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.modal.DashboardStats;
import com.backend.service.StatsService;

import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStats> getDashboardStats() {
        DashboardStats stats = statsService.getDashboardStats();
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    @GetMapping("/weekly")
    public ResponseEntity<Map<String, Object>> getWeeklyStats() {
        Map<String, Object> weeklyStats = statsService.getWeeklyStats();
        return new ResponseEntity<>(weeklyStats, HttpStatus.OK);
    }
    
    @GetMapping("/yearly")
    public ResponseEntity<Map<String, Object>> getYearlyStats() {
        Map<String, Object> yearlyStats = statsService.getYearlyStats();
        return new ResponseEntity<>(yearlyStats, HttpStatus.OK);
    }
}