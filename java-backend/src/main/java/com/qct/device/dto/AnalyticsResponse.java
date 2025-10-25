package com.qct.device.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AnalyticsResponse {
    
    private Long id;
    private String deviceId;
    private String metricType;
    private BigDecimal value;
    private String unit;
    private String description;
    private LocalDateTime timestamp;
    
    // Aggregated metrics
    private BigDecimal averageValue;
    private BigDecimal maxValue;
    private BigDecimal minValue;
    private Long totalCount;
}