-- PostgreSQL initialization script for QCT Device Management
-- This script creates the database schema for device management

CREATE DATABASE qct_devices;

\c qct_devices;

-- Create user for the application
CREATE USER qct_user WITH PASSWORD 'qct_password';
GRANT ALL PRIVILEGES ON DATABASE qct_devices TO qct_user;

-- Create devices table
CREATE TABLE devices (
    id BIGSERIAL PRIMARY KEY,
    device_id VARCHAR(50) UNIQUE NOT NULL,
    oem_id VARCHAR(50) NOT NULL,
    device_type VARCHAR(50) NOT NULL,
    configuration TEXT,
    firmware VARCHAR(100),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create device_metrics table for analytics
CREATE TABLE device_metrics (
    id BIGSERIAL PRIMARY KEY,
    device_id VARCHAR(50) NOT NULL,
    metric_type VARCHAR(30) NOT NULL,
    value DECIMAL(10,2) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (device_id) REFERENCES devices(device_id)
);

-- Create indexes for performance
CREATE INDEX idx_devices_oem_id ON devices(oem_id);
CREATE INDEX idx_devices_device_type ON devices(device_type);
CREATE INDEX idx_devices_status ON devices(status);
CREATE INDEX idx_device_metrics_device_id ON device_metrics(device_id);
CREATE INDEX idx_device_metrics_timestamp ON device_metrics(timestamp);

-- Grant permissions to application user
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO qct_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO qct_user;

-- Insert sample data
INSERT INTO devices (device_id, oem_id, device_type, configuration, firmware, status) VALUES
('DEV001', 'SAMSUNG', 'SMARTPHONE', '{"cpu": "Snapdragon 8 Gen 2", "ram": "12GB"}', 'v1.2.3', 'ACTIVE'),
('DEV002', 'XIAOMI', 'TABLET', '{"cpu": "Snapdragon 870", "ram": "8GB"}', 'v1.1.0', 'ACTIVE'),
('DEV003', 'ONEPLUS', 'SMARTPHONE', '{"cpu": "Snapdragon 8 Gen 1", "ram": "16GB"}', 'v2.0.1', 'INACTIVE');

-- Insert sample metrics
INSERT INTO device_metrics (device_id, metric_type, value) VALUES
('DEV001', 'CPU_USAGE', 45.5),
('DEV001', 'MEMORY_USAGE', 67.2),
('DEV002', 'CPU_USAGE', 32.1),
('DEV002', 'MEMORY_USAGE', 54.8),
('DEV003', 'CPU_USAGE', 78.9),
('DEV003', 'MEMORY_USAGE', 89.3);