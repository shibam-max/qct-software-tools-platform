package com.qct.device.service;

import com.qct.device.dto.DeviceRequest;
import com.qct.device.dto.DeviceResponse;
import com.qct.device.model.Device;
import com.qct.device.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DeviceService {
    
    private final DeviceRepository deviceRepository;
    
    public DeviceResponse configureDevice(DeviceRequest request) {
        log.info("Configuring device: {}", request.getDeviceId());
        
        Device device = new Device();
        device.setDeviceId(request.getDeviceId());
        device.setOemId(request.getOemId());
        device.setDeviceType(request.getDeviceType());
        device.setConfiguration(request.getConfiguration());
        device.setFirmware(request.getFirmware());
        device.setStatus(request.getStatus() != null ? request.getStatus() : "ACTIVE");
        
        device = deviceRepository.save(device);
        
        return mapToResponse(device);
    }
    
    @Transactional(readOnly = true)
    public Optional<DeviceResponse> getDevice(String deviceId) {
        return deviceRepository.findByDeviceId(deviceId)
                .map(this::mapToResponse);
    }
    
    @Transactional(readOnly = true)
    public List<DeviceResponse> getDevicesByOem(String oemId) {
        return deviceRepository.findByOemId(oemId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    
    public DeviceResponse updateDeviceStatus(String deviceId, String status) {
        Device device = deviceRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found: " + deviceId));
        
        device.setStatus(status);
        device = deviceRepository.save(device);
        
        return mapToResponse(device);
    }
    
    private DeviceResponse mapToResponse(Device device) {
        return DeviceResponse.builder()
                .deviceId(device.getDeviceId())
                .oemId(device.getOemId())
                .deviceType(device.getDeviceType())
                .configuration(device.getConfiguration())
                .firmware(device.getFirmware())
                .status(device.getStatus())
                .createdAt(device.getCreatedAt())
                .updatedAt(device.getUpdatedAt())
                .build();
    }
}