@echo off
REM QCT Software Tools Platform - Build All Services (Windows)
echo 🚀 Building QCT Software Tools Platform...

REM Check if Docker is running
docker info >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Docker is not running. Please start Docker and try again.
    exit /b 1
)

REM Build Java Backend
echo [INFO] Building Java Device Management Service...
cd java-backend
call mvn clean package -DskipTests
if %errorlevel% neq 0 (
    echo [ERROR] Java service build failed
    exit /b 1
)
echo [INFO] ✅ Java service built successfully
cd ..

REM Build .NET Backend
echo [INFO] Building .NET User Management Service...
cd dotnet-backend
call dotnet build --configuration Release
if %errorlevel% neq 0 (
    echo [ERROR] .NET service build failed
    exit /b 1
)
echo [INFO] ✅ .NET service built successfully
cd ..

REM Build Docker images
echo [INFO] Building Docker images...
docker-compose build
if %errorlevel% neq 0 (
    echo [ERROR] Docker build failed
    exit /b 1
)
echo [INFO] ✅ Docker images built successfully

echo [INFO] 🎉 All services built successfully!
echo [INFO] Run 'docker-compose up' to start the platform