package com.qct.device.repository;

import com.qct.device.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    
    Optional<Device> findByDeviceId(String deviceId);
    
    List<Device> findByOemId(String oemId);
    
    List<Device> findByStatus(String status);
    
    List<Device> findByDeviceType(String deviceType);
    
    boolean existsByDeviceId(String deviceId);
}