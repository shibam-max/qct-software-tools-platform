#!/bin/bash

# QCT Software Tools Platform - Production Deployment Script
echo "🚀 Deploying QCT Software Tools Platform to Production..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check required environment variables
if [ -z "$DB_USERNAME" ] || [ -z "$DB_PASSWORD" ] || [ -z "$SQL_PASSWORD" ]; then
    print_error "Required environment variables not set:"
    echo "  - DB_USERNAME: PostgreSQL username"
    echo "  - DB_PASSWORD: PostgreSQL password"
    echo "  - SQL_PASSWORD: SQL Server password"
    exit 1
fi

# Build and tag images
print_status "Building production images..."
docker build -t qct/device-service:latest ./java-backend
docker build -t qct/user-service:latest ./dotnet-backend

# Deploy with production configuration
print_status "Starting production deployment..."
docker-compose -f docker/production.yml up -d

# Wait for services to be healthy
print_status "Waiting for services to be healthy..."
sleep 60

# Health checks
print_status "Performing health checks..."
if curl -f http://localhost:8080/api/v1/devices/health > /dev/null 2>&1; then
    print_status "✅ Java Device Service is healthy"
else
    print_error "❌ Java Device Service health check failed"
    exit 1
fi

if curl -f http://localhost:5000/api/v1/users/health > /dev/null 2>&1; then
    print_status "✅ .NET User Service is healthy"
else
    print_error "❌ .NET User Service health check failed"
    exit 1
fi

print_status "🎉 Production deployment completed successfully!"
print_status "Services available at:"
print_status "  - Device Management: http://localhost:8080"
print_status "  - User Management: http://localhost:5000"
print_status "  - Load Balancer: http://localhost"