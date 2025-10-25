package com.qct.device.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeviceRequest {
    
    @NotBlank(message = "Device ID is required")
    private String deviceId;
    
    @NotBlank(message = "OEM ID is required")
    private String oemId;
    
    @NotBlank(message = "Device type is required")
    private String deviceType;
    
    private String configuration;
    private String firmware;
    private String status;
}