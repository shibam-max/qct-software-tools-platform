package com.qct.device.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class DeviceResponse {
    
    private String deviceId;
    private String oemId;
    private String deviceType;
    private String configuration;
    private String firmware;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}