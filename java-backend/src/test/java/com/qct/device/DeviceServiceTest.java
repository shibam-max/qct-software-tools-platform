package com.qct.device;

import com.qct.device.dto.DeviceRequest;
import com.qct.device.dto.DeviceResponse;
import com.qct.device.model.Device;
import com.qct.device.repository.DeviceRepository;
import com.qct.device.service.DeviceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceService deviceService;

    @Test
    void configureDevice_ShouldReturnDeviceResponse() {
        DeviceRequest request = new DeviceRequest();
        request.setDeviceId("DEV001");
        request.setOemId("OEM001");
        request.setDeviceType("SMARTPHONE");

        Device savedDevice = new Device();
        savedDevice.setDeviceId("DEV001");
        savedDevice.setStatus("ACTIVE");

        when(deviceRepository.save(any(Device.class))).thenReturn(savedDevice);

        DeviceResponse response = deviceService.configureDevice(request);

        assertNotNull(response);
        assertEquals("DEV001", response.getDeviceId());
        verify(deviceRepository).save(any(Device.class));
    }
}