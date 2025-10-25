# 🚀 QCT Software Tools Platform - Deployment Guide

## Overview

This guide covers deployment options for the QCT Software Tools Platform, including local development, Docker containerization, and production deployment strategies.

## 🏗️ Architecture Overview

```
┌─────────────────┐    ┌─────────────────┐
│   Java Service  │    │  .NET Service   │
│   (Port 8080)   │    │   (Port 5000)   │
│                 │    │                 │
│ Device Mgmt     │    │ User Mgmt       │
│ Analytics       │    │ Notifications   │
└─────────────────┘    └─────────────────┘
         │                       │
         └───────────┬───────────┘
                     │
    ┌────────────────┼────────────────┐
    │                │                │
┌───▼───┐    ┌──────▼──────┐    ┌────▼────┐
│PostgreSQL│  │ SQL Server  │    │  Redis  │
│(Port 5432)│  │(Port 1433) │    │(Port 6379)│
└───────────┘  └─────────────┘    └─────────┘
```

## 🛠️ Prerequisites

### Required Software
- **Docker Desktop** 4.0+ with Docker Compose
- **Java 17+** (for local development)
- **.NET 8.0 SDK** (for local development)
- **Maven 3.8+** (for Java builds)

### System Requirements
- **RAM**: 8GB minimum, 16GB recommended
- **Storage**: 10GB free space
- **CPU**: 4 cores recommended

## 🚀 Quick Start

### Option 1: Docker Compose (Recommended)

```bash
# Clone and navigate to project
cd qct-software-tools-platform

# Build all services
./scripts/build-all.sh    # Linux/Mac
# OR
scripts\build-all.bat     # Windows

# Start the platform
docker-compose up -d

# Check service health
curl http://localhost:8080/api/v1/devices/health
curl http://localhost:5000/api/v1/users/health
```

### Option 2: Local Development

```bash
# Start databases only
docker-compose up -d postgres sqlserver redis

# Start Java service
cd java-backend
mvn spring-boot:run

# Start .NET service (in new terminal)
cd dotnet-backend
dotnet run
```

### Option 3: Windows Local Development

```batch
# Use the provided script
scripts\start-local.bat
```

## 📊 Service Endpoints

### Java Device Management Service (Port 8080)
- **Health Check**: `GET /api/v1/devices/health`
- **API Documentation**: `http://localhost:8080/swagger-ui.html`
- **Device Configuration**: `POST /api/v1/devices/configure`
- **Analytics**: `GET /api/v1/analytics/device/{deviceId}`

### .NET User Management Service (Port 5000)
- **Health Check**: `GET /api/v1/users/health`
- **API Documentation**: `http://localhost:5000/swagger`
- **User Registration**: `POST /api/v1/users/register`
- **Notifications**: `GET /api/v1/notifications/user/{userId}`

## 🗄️ Database Configuration

### PostgreSQL (Java Service)
```yaml
Database: qct_devices
Host: localhost:5432
Username: qct_user
Password: qct_password
```

### SQL Server (.NET Service)
```yaml
Database: QCT_Users
Host: localhost:1433
Username: sa
Password: YourStrong@Passw0rd
```

### Redis (Caching)
```yaml
Host: localhost:6379
No authentication required
```

## 🔧 Environment Configuration

### Java Service Environment Variables
```bash
SPRING_PROFILES_ACTIVE=docker
DATABASE_URL=jdbc:postgresql://postgres:5432/qct_devices
REDIS_URL=redis://redis:6379
```

### .NET Service Environment Variables
```bash
ASPNETCORE_ENVIRONMENT=Docker
ConnectionStrings__DefaultConnection=Server=sqlserver;Database=QCT_Users;User Id=sa;Password=YourStrong@Passw0rd;TrustServerCertificate=true;
```

## 🐳 Docker Configuration

### Building Images
```bash
# Build all images
docker-compose build

# Build specific service
docker-compose build device-service
docker-compose build user-service
```

### Managing Containers
```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f device-service
docker-compose logs -f user-service

# Stop all services
docker-compose down

# Remove volumes (reset databases)
docker-compose down -v
```

## 📈 Performance Tuning

### Java Service Optimization
```yaml
# JVM Options
JAVA_OPTS: >
  -Xms512m
  -Xmx2g
  -XX:+UseG1GC
  -XX:MaxGCPauseMillis=200
```

### Database Connection Pooling
```yaml
# PostgreSQL (Java)
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5

# SQL Server (.NET)
"ConnectionStrings": {
  "DefaultConnection": "...;Max Pool Size=100;Min Pool Size=10;"
}
```

### Redis Caching
```yaml
# Cache TTL Configuration
spring.cache.redis.time-to-live=1800000  # 30 minutes
```

## 🔒 Security Configuration

### Network Security
```yaml
# Docker network isolation
networks:
  qct-network:
    driver: bridge
    internal: false
```

### Database Security
- Use strong passwords in production
- Enable SSL/TLS connections
- Implement connection encryption
- Regular security updates

## 📊 Monitoring & Logging

### Health Checks
```bash
# Service health endpoints
curl http://localhost:8080/actuator/health
curl http://localhost:5000/health
```

### Log Aggregation
```yaml
# Docker logging configuration
logging:
  driver: "json-file"
  options:
    max-size: "10m"
    max-file: "3"
```

## 🚀 Production Deployment

### Kubernetes Deployment
```yaml
# Example deployment configuration
apiVersion: apps/v1
kind: Deployment
metadata:
  name: qct-device-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: qct-device-service
  template:
    metadata:
      labels:
        app: qct-device-service
    spec:
      containers:
      - name: device-service
        image: qct/device-service:latest
        ports:
        - containerPort: 8080
```

### Load Balancing
- Use NGINX or HAProxy for load balancing
- Configure health checks
- Implement circuit breakers
- Set up auto-scaling policies

### Database High Availability
- PostgreSQL: Master-slave replication
- SQL Server: Always On Availability Groups
- Redis: Cluster mode with replication

## 🔧 Troubleshooting

### Common Issues

#### Port Conflicts
```bash
# Check port usage
netstat -tulpn | grep :8080
netstat -tulpn | grep :5000

# Kill processes using ports
sudo kill -9 $(lsof -t -i:8080)
sudo kill -9 $(lsof -t -i:5000)
```

#### Database Connection Issues
```bash
# Test PostgreSQL connection
docker exec -it qct-postgres psql -U qct_user -d qct_devices

# Test SQL Server connection
docker exec -it qct-sqlserver /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P 'YourStrong@Passw0rd'
```

#### Memory Issues
```bash
# Increase Docker memory limits
# Docker Desktop -> Settings -> Resources -> Memory: 8GB+

# Monitor container memory usage
docker stats
```

### Log Analysis
```bash
# View application logs
docker-compose logs -f --tail=100 device-service
docker-compose logs -f --tail=100 user-service

# View database logs
docker-compose logs postgres
docker-compose logs sqlserver
```

## 📋 Deployment Checklist

### Pre-deployment
- [ ] All tests passing
- [ ] Security scan completed
- [ ] Performance benchmarks met
- [ ] Database migrations ready
- [ ] Environment variables configured

### Deployment
- [ ] Build and tag Docker images
- [ ] Deploy to staging environment
- [ ] Run integration tests
- [ ] Deploy to production
- [ ] Verify health checks

### Post-deployment
- [ ] Monitor application metrics
- [ ] Check error logs
- [ ] Verify database connectivity
- [ ] Test critical user flows
- [ ] Update documentation

## 🎯 Performance Benchmarks

### Expected Performance
- **API Response Time**: <50ms (P95)
- **Throughput**: 10,000+ requests/second
- **Database Query Time**: <10ms average
- **Cache Hit Rate**: >95%
- **Uptime**: 99.99%

### Load Testing
```bash
# Use Apache Bench for basic load testing
ab -n 10000 -c 100 http://localhost:8080/api/v1/devices/health

# Use JMeter for comprehensive testing
jmeter -n -t load-test-plan.jmx -l results.jtl
```

---

**Ready for enterprise deployment at Qualcomm scale! 🛠️🚀**