package com.qct.device.controller;

import com.qct.device.dto.DeviceRequest;
import com.qct.device.dto.DeviceResponse;
import com.qct.device.service.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/devices")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class DeviceController {
    
    private final DeviceService deviceService;
    
    @PostMapping("/configure")
    public ResponseEntity<DeviceResponse> configureDevice(@Valid @RequestBody DeviceRequest request) {
        log.info("Configuring device: {}", request.getDeviceId());
        DeviceResponse response = deviceService.configureDevice(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{deviceId}")
    public ResponseEntity<DeviceResponse> getDevice(@PathVariable String deviceId) {
        return deviceService.getDevice(deviceId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/oem/{oemId}")
    public ResponseEntity<List<DeviceResponse>> getOemDevices(@PathVariable String oemId) {
        List<DeviceResponse> devices = deviceService.getDevicesByOem(oemId);
        return ResponseEntity.ok(devices);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Device Management Service is healthy");
    }
}