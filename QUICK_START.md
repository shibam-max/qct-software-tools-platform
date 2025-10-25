# 🚀 QCT Software Tools Platform - Quick Start Guide

## Overview

Get the QCT Software Tools Platform running in under 5 minutes with this quick start guide.

## Prerequisites

- **Docker Desktop** installed and running
- **8GB RAM** available
- **10GB free disk space**

## 🏃‍♂️ Quick Start (Docker)

### 1. Start the Platform
```bash
# Clone the repository (if not already done)
cd qct-software-tools-platform

# Start all services
docker-compose up -d
```

### 2. Wait for Services to Start
```bash
# Check service health (wait ~60 seconds for full startup)
curl http://localhost:8080/api/v1/devices/health
curl http://localhost:5000/api/v1/users/health
```

### 3. Access the APIs

#### Java Device Management Service
- **Base URL**: http://localhost:8080
- **API Docs**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/api/v1/devices/health

#### .NET User Management Service  
- **Base URL**: http://localhost:5000
- **API Docs**: http://localhost:5000/swagger
- **Health Check**: http://localhost:5000/api/v1/users/health

## 🧪 Test the APIs

### Device Management (Java Service)

#### Configure a Device
```bash
curl -X POST http://localhost:8080/api/v1/devices/configure \
  -H "Content-Type: application/json" \
  -d '{
    "deviceId": "TEST001",
    "oemId": "SAMSUNG",
    "deviceType": "SMARTPHONE",
    "configuration": "{\"cpu\": \"Snapdragon 8 Gen 3\", \"ram\": \"16GB\"}",
    "firmware": "v2.1.0"
  }'
```

#### Get Device Details
```bash
curl http://localhost:8080/api/v1/devices/TEST001
```

#### Record Analytics
```bash
curl -X POST http://localhost:8080/api/v1/analytics/metrics \
  -H "Content-Type: application/json" \
  -d '{
    "deviceId": "TEST001",
    "metricType": "CPU_USAGE",
    "value": 45.5
  }'
```

### User Management (.NET Service)

#### Register a User
```bash
curl -X POST http://localhost:5000/api/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@qct.com",
    "password": "SecurePass123!",
    "firstName": "Test",
    "lastName": "User"
  }'
```

#### Authenticate User
```bash
curl -X POST http://localhost:5000/api/v1/users/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@qct.com",
    "password": "SecurePass123!"
  }'
```

#### Send Notification
```bash
curl -X POST http://localhost:5000/api/v1/notifications/send \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "title": "Device Alert",
    "message": "Device TEST001 CPU usage is high",
    "type": "DeviceAlert",
    "priority": "High"
  }'
```

## 📊 View Sample Data

The platform comes with pre-loaded sample data:

### Sample Devices
- **DEV001**: Samsung Smartphone (Snapdragon 8 Gen 2)
- **DEV002**: Xiaomi Tablet (Snapdragon 870)  
- **DEV003**: OnePlus Smartphone (Snapdragon 8 Gen 1)

### Sample Users
- **admin@qct.com**: Administrator user
- **dev@qct.com**: Developer user
- **test@qct.com**: Test user

## 🔧 Development Mode

### Start Services Locally (Windows)
```batch
# Use the provided script
scripts\start-local.bat
```

### Start Services Locally (Linux/Mac)
```bash
# Start databases
docker-compose up -d postgres sqlserver redis

# Start Java service
cd java-backend
mvn spring-boot:run &

# Start .NET service  
cd dotnet-backend
dotnet run &
```

## 🛠️ Build from Source

### Build All Services
```bash
# Linux/Mac
./scripts/build-all.sh

# Windows
scripts\build-all.bat
```

### Build Individual Services
```bash
# Java service
cd java-backend
mvn clean package

# .NET service
cd dotnet-backend
dotnet build --configuration Release
```

## 📈 Performance Testing

### Quick Load Test
```bash
# Test device configuration endpoint
ab -n 1000 -c 10 -H "Content-Type: application/json" \
   -p test-device.json \
   http://localhost:8080/api/v1/devices/configure

# Test user authentication
ab -n 1000 -c 10 -H "Content-Type: application/json" \
   -p test-auth.json \
   http://localhost:5000/api/v1/users/authenticate
```

### Expected Results
- **Response Time**: <50ms (P95)
- **Throughput**: 1000+ requests/second
- **Error Rate**: <0.1%

## 🔍 Monitoring

### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f device-service
docker-compose logs -f user-service
```

### Database Access
```bash
# PostgreSQL (Java service data)
docker exec -it qct-postgres psql -U qct_user -d qct_devices

# SQL Server (.NET service data)
docker exec -it qct-sqlserver /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P 'YourStrong@Passw0rd'

# Redis (cache data)
docker exec -it qct-redis redis-cli
```

## 🛑 Stop the Platform

```bash
# Stop all services
docker-compose down

# Stop and remove volumes (reset databases)
docker-compose down -v
```

## 🆘 Troubleshooting

### Port Conflicts
```bash
# Check if ports are in use
netstat -tulpn | grep :8080
netstat -tulpn | grep :5000

# Kill processes using ports
sudo kill -9 $(lsof -t -i:8080)
sudo kill -9 $(lsof -t -i:5000)
```

### Service Not Starting
```bash
# Check Docker status
docker ps -a

# View service logs
docker-compose logs device-service
docker-compose logs user-service

# Restart specific service
docker-compose restart device-service
```

### Database Connection Issues
```bash
# Restart databases
docker-compose restart postgres sqlserver redis

# Check database logs
docker-compose logs postgres
docker-compose logs sqlserver
```

## 📚 Next Steps

1. **Explore API Documentation**: Visit Swagger UI endpoints
2. **Review Architecture**: Check `/docs/DEPLOYMENT_GUIDE.md`
3. **Performance Benchmarks**: See `/docs/PERFORMANCE_BENCHMARKS.md`
4. **Security Guide**: Review security configurations
5. **Production Deployment**: Follow deployment best practices

---

**Ready to explore enterprise-grade microservices! 🛠️🚀**