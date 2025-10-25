#!/bin/bash

# QCT Software Tools Platform - Build All Services
echo "🚀 Building QCT Software Tools Platform..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    print_error "Docker is not running. Please start Docker and try again."
    exit 1
fi

# Build Java Backend
print_status "Building Java Device Management Service..."
cd java-backend
if mvn clean package -DskipTests; then
    print_status "✅ Java service built successfully"
else
    print_error "❌ Java service build failed"
    exit 1
fi
cd ..

# Build .NET Backend
print_status "Building .NET User Management Service..."
cd dotnet-backend
if dotnet build --configuration Release; then
    print_status "✅ .NET service built successfully"
else
    print_error "❌ .NET service build failed"
    exit 1
fi
cd ..

# Build Docker images
print_status "Building Docker images..."
if docker-compose build; then
    print_status "✅ Docker images built successfully"
else
    print_error "❌ Docker build failed"
    exit 1
fi

print_status "🎉 All services built successfully!"
print_status "Run 'docker-compose up' to start the platform"