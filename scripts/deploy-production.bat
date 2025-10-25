@echo off
REM QCT Software Tools Platform - Production Deployment Script (Windows)
echo 🚀 Deploying QCT Software Tools Platform to Production...

REM Check required environment variables
if "%DB_USERNAME%"=="" (
    echo [ERROR] DB_USERNAME environment variable not set
    exit /b 1
)
if "%DB_PASSWORD%"=="" (
    echo [ERROR] DB_PASSWORD environment variable not set
    exit /b 1
)
if "%SQL_PASSWORD%"=="" (
    echo [ERROR] SQL_PASSWORD environment variable not set
    exit /b 1
)

REM Build and tag images
echo [INFO] Building production images...
docker build -t qct/device-service:latest ./java-backend
if %errorlevel% neq 0 (
    echo [ERROR] Failed to build Java service image
    exit /b 1
)

docker build -t qct/user-service:latest ./dotnet-backend
if %errorlevel% neq 0 (
    echo [ERROR] Failed to build .NET service image
    exit /b 1
)

REM Deploy with production configuration
echo [INFO] Starting production deployment...
docker-compose -f docker/production.yml up -d
if %errorlevel% neq 0 (
    echo [ERROR] Failed to start production services
    exit /b 1
)

REM Wait for services to be healthy
echo [INFO] Waiting for services to be healthy...
timeout /t 60 /nobreak >nul

REM Health checks
echo [INFO] Performing health checks...
curl -f http://localhost:8080/api/v1/devices/health >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Java Device Service health check failed
    exit /b 1
)
echo [INFO] ✅ Java Device Service is healthy

curl -f http://localhost:5000/api/v1/users/health >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] .NET User Service health check failed
    exit /b 1
)
echo [INFO] ✅ .NET User Service is healthy

echo [INFO] 🎉 Production deployment completed successfully!
echo [INFO] Services available at:
echo [INFO]   - Device Management: http://localhost:8080
echo [INFO]   - User Management: http://localhost:5000
echo [INFO]   - Load Balancer: http://localhost