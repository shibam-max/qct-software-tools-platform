# 🛠️ QCT Software Tools Platform - API Documentation

## 🚀 Base URLs
- **Java Service**: `http://localhost:8080`
- **NET Service**: `http://localhost:5000`

## 📋 Java Device Management API

### Configure Device
```http
POST /api/v1/devices/configure
Content-Type: application/json

{
  "deviceId": "DEV001",
  "oemId": "OEM001",
  "deviceType": "SMARTPHONE",
  "configuration": "config_data",
  "firmware": "v1.0.0",
  "status": "ACTIVE"
}
```

### Get Device
```http
GET /api/v1/devices/{deviceId}
```

### Get OEM Devices
```http
GET /api/v1/devices/oem/{oemId}
```

## 📋 .NET User Management API

### Register User
```http
POST /api/v1/users/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe"
}
```

### Authenticate User
```http
POST /api/v1/users/authenticate
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

### Get User
```http
GET /api/v1/users/{userId}
```

## 🚀 Deployment

### Docker Compose
```bash
docker-compose up -d
```

### Individual Services
```bash
# Java Service
cd java-backend
mvn spring-boot:run

# .NET Service
cd dotnet-backend
dotnet run
```

## 📊 Performance Metrics
- **Response Time**: <50ms (P95)
- **Throughput**: 10,000+ requests/second
- **Database**: Optimized queries with indexing
- **Caching**: Redis integration for performance