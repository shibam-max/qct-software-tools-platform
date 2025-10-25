package com.qct.device.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "devices")
@Data
public class Device {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "device_id", unique = true, nullable = false)
    private String deviceId;
    
    @Column(name = "oem_id", nullable = false)
    private String oemId;
    
    @Column(name = "device_type", nullable = false)
    private String deviceType;
    
    @Column(name = "configuration", columnDefinition = "TEXT")
    private String configuration;
    
    @Column(name = "firmware")
    private String firmware;
    
    @Column(name = "status")
    private String status;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}