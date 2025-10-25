package com.qct.device;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qct.device.controller.DeviceController;
import com.qct.device.dto.DeviceRequest;
import com.qct.device.dto.DeviceResponse;
import com.qct.device.service.DeviceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DeviceController.class)
class DeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeviceService deviceService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void configureDevice_ShouldReturnCreatedDevice() throws Exception {
        DeviceRequest request = new DeviceRequest();
        request.setDeviceId("DEV001");
        request.setOemId("OEM001");
        request.setDeviceType("SMARTPHONE");

        DeviceResponse response = DeviceResponse.builder()
                .deviceId("DEV001")
                .oemId("OEM001")
                .deviceType("SMARTPHONE")
                .status("ACTIVE")
                .build();

        when(deviceService.configureDevice(any(DeviceRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/devices/configure")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deviceId").value("DEV001"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void getDevice_ShouldReturnDevice_WhenExists() throws Exception {
        DeviceResponse response = DeviceResponse.builder()
                .deviceId("DEV001")
                .oemId("OEM001")
                .build();

        when(deviceService.getDevice("DEV001")).thenReturn(Optional.of(response));

        mockMvc.perform(get("/api/v1/devices/DEV001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deviceId").value("DEV001"));
    }

    @Test
    void healthCheck_ShouldReturnHealthy() throws Exception {
        mockMvc.perform(get("/api/v1/devices/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Device Management Service is healthy"));
    }
}