package com.qct.device.controller;

import com.qct.device.dto.AnalyticsRequest;
import com.qct.device.dto.AnalyticsResponse;
import com.qct.device.service.AnalyticsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AnalyticsController {
    
    private final AnalyticsService analyticsService;
    
    @PostMapping("/metrics")
    public ResponseEntity<AnalyticsResponse> recordMetric(@Valid @RequestBody AnalyticsRequest request) {
        log.info("Recording metric for device: {}", request.getDeviceId());
        AnalyticsResponse response = analyticsService.recordMetric(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<AnalyticsResponse>> getDeviceMetrics(@PathVariable String deviceId) {
        log.info("Fetching metrics for device: {}", deviceId);
        List<AnalyticsResponse> metrics = analyticsService.getDeviceMetrics(deviceId);
        return ResponseEntity.ok(metrics);
    }
    
    @GetMapping("/oem/{oemId}/summary")
    public ResponseEntity<List<AnalyticsResponse>> getOemAnalytics(@PathVariable String oemId) {
        log.info("Fetching analytics summary for OEM: {}", oemId);
        List<AnalyticsResponse> analytics = analyticsService.getOemAnalytics(oemId);
        return ResponseEntity.ok(analytics);
    }
    
    @GetMapping("/performance/{deviceId}")
    public ResponseEntity<AnalyticsResponse> getPerformanceMetrics(@PathVariable String deviceId) {
        log.info("Fetching performance metrics for device: {}", deviceId);
        AnalyticsResponse performance = analyticsService.getPerformanceMetrics(deviceId);
        return ResponseEntity.ok(performance);
    }
}