package com.qct.device.repository;

import com.qct.device.model.DeviceMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeviceMetricRepository extends JpaRepository<DeviceMetric, Long> {
    
    List<DeviceMetric> findByDeviceIdOrderByTimestampDesc(String deviceId);
    
    List<DeviceMetric> findByDeviceIdAndMetricType(String deviceId, String metricType);
    
    @Query("SELECT dm FROM DeviceMetric dm WHERE dm.deviceId IN " +
           "(SELECT d.deviceId FROM Device d WHERE d.oemId = :oemId) " +
           "ORDER BY dm.timestamp DESC")
    List<DeviceMetric> findByOemId(@Param("oemId") String oemId);
    
    @Query("SELECT dm FROM DeviceMetric dm WHERE dm.deviceId = :deviceId " +
           "AND dm.timestamp >= :startTime ORDER BY dm.timestamp DESC")
    List<DeviceMetric> findRecentMetrics(@Param("deviceId") String deviceId, 
                                       @Param("startTime") LocalDateTime startTime);
    
    @Query("SELECT AVG(dm.value) FROM DeviceMetric dm WHERE dm.deviceId = :deviceId " +
           "AND dm.metricType = :metricType")
    Double getAverageValue(@Param("deviceId") String deviceId, 
                          @Param("metricType") String metricType);
    
    @Query("SELECT MAX(dm.value) FROM DeviceMetric dm WHERE dm.deviceId = :deviceId " +
           "AND dm.metricType = :metricType")
    Double getMaxValue(@Param("deviceId") String deviceId, 
                      @Param("metricType") String metricType);
    
    @Query("SELECT MIN(dm.value) FROM DeviceMetric dm WHERE dm.deviceId = :deviceId " +
           "AND dm.metricType = :metricType")
    Double getMinValue(@Param("deviceId") String deviceId, 
                      @Param("metricType") String metricType);
}