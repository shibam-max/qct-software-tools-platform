package com.qct.device.service;

import com.qct.device.dto.AnalyticsRequest;
import com.qct.device.dto.AnalyticsResponse;
import com.qct.device.model.DeviceMetric;
import com.qct.device.repository.DeviceMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AnalyticsService {
    
    private final DeviceMetricRepository deviceMetricRepository;
    
    public AnalyticsResponse recordMetric(AnalyticsRequest request) {
        log.info("Recording metric {} for device {}", request.getMetricType(), request.getDeviceId());
        
        DeviceMetric metric = new DeviceMetric();
        metric.setDeviceId(request.getDeviceId());
        metric.setMetricType(request.getMetricType());
        metric.setValue(request.getValue());
        metric.setUnit(request.getUnit());
        metric.setDescription(request.getDescription());
        
        metric = deviceMetricRepository.save(metric);
        
        return mapToResponse(metric);
    }
    
    @Transactional(readOnly = true)
    @Cacheable(value = "deviceMetrics", key = "#deviceId")
    public List<AnalyticsResponse> getDeviceMetrics(String deviceId) {
        log.info("Fetching metrics for device: {}", deviceId);
        
        return deviceMetricRepository.findByDeviceIdOrderByTimestampDesc(deviceId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    
    @Transactional(readOnly = true)
    @Cacheable(value = "oemAnalytics", key = "#oemId")
    public List<AnalyticsResponse> getOemAnalytics(String oemId) {
        log.info("Fetching analytics for OEM: {}", oemId);
        
        return deviceMetricRepository.findByOemId(oemId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    
    @Transactional(readOnly = true)
    @Cacheable(value = "performanceMetrics", key = "#deviceId")
    public AnalyticsResponse getPerformanceMetrics(String deviceId) {
        log.info("Calculating performance metrics for device: {}", deviceId);
        
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        List<DeviceMetric> recentMetrics = deviceMetricRepository.findRecentMetrics(deviceId, oneDayAgo);
        
        if (recentMetrics.isEmpty()) {
            return AnalyticsResponse.builder()
                    .deviceId(deviceId)
                    .metricType("PERFORMANCE_SUMMARY")
                    .totalCount(0L)
                    .build();
        }
        
        // Calculate aggregated metrics
        Double avgCpuUsage = deviceMetricRepository.getAverageValue(deviceId, "CPU_USAGE");
        Double maxCpuUsage = deviceMetricRepository.getMaxValue(deviceId, "CPU_USAGE");
        Double minCpuUsage = deviceMetricRepository.getMinValue(deviceId, "CPU_USAGE");
        
        return AnalyticsResponse.builder()
                .deviceId(deviceId)
                .metricType("PERFORMANCE_SUMMARY")
                .averageValue(avgCpuUsage != null ? BigDecimal.valueOf(avgCpuUsage) : BigDecimal.ZERO)
                .maxValue(maxCpuUsage != null ? BigDecimal.valueOf(maxCpuUsage) : BigDecimal.ZERO)
                .minValue(minCpuUsage != null ? BigDecimal.valueOf(minCpuUsage) : BigDecimal.ZERO)
                .totalCount((long) recentMetrics.size())
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    private AnalyticsResponse mapToResponse(DeviceMetric metric) {
        return AnalyticsResponse.builder()
                .id(metric.getId())
                .deviceId(metric.getDeviceId())
                .metricType(metric.getMetricType())
                .value(metric.getValue())
                .unit(metric.getUnit())
                .description(metric.getDescription())
                .timestamp(metric.getTimestamp())
                .build();
    }
}