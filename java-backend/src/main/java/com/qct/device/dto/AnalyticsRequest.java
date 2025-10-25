package com.qct.device.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AnalyticsRequest {
    
    @NotBlank(message = "Device ID is required")
    private String deviceId;
    
    @NotBlank(message = "Metric type is required")
    private String metricType;
    
    @NotNull(message = "Value is required")
    private BigDecimal value;
    
    private String unit;
    private String description;
}