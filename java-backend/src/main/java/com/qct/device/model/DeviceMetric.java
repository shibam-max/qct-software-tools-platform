package com.qct.device.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "device_metrics")
@Data
public class DeviceMetric {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "device_id", nullable = false)
    private String deviceId;
    
    @Column(name = "metric_type", nullable = false)
    private String metricType;
    
    @Column(name = "value", nullable = false, precision = 10, scale = 2)
    private BigDecimal value;
    
    @Column(name = "unit")
    private String unit;
    
    @Column(name = "description")
    private String description;
    
    @CreationTimestamp
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}