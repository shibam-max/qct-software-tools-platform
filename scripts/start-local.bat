@echo off
REM QCT Software Tools Platform - Start Local Development Environment
echo 🚀 Starting QCT Software Tools Platform locally...

REM Check if Docker is running
docker info >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Docker is not running. Please start Docker and try again.
    exit /b 1
)

REM Start databases first
echo [INFO] Starting databases...
docker-compose up -d postgres sqlserver redis

REM Wait for databases to be ready
echo [INFO] Waiting for databases to be ready...
timeout /t 30 /nobreak >nul

REM Start Java service
echo [INFO] Starting Java Device Management Service...
cd java-backend
start "Java Service" cmd /k "mvn spring-boot:run"
cd ..

REM Wait a bit for Java service to start
timeout /t 10 /nobreak >nul

REM Start .NET service
echo [INFO] Starting .NET User Management Service...
cd dotnet-backend
start ".NET Service" cmd /k "dotnet run"
cd ..

echo [INFO] 🎉 All services are starting!
echo [INFO] Java Service: http://localhost:8080
echo [INFO] .NET Service: http://localhost:5000
echo [INFO] Java API Docs: http://localhost:8080/swagger-ui.html
echo [INFO] .NET API Docs: http://localhost:5000/swagger
echo [INFO] Press any key to stop all services...
pause >nul

REM Stop all services
echo [INFO] Stopping all services...
docker-compose down
taskkill /f /im java.exe 2>nul
taskkill /f /im dotnet.exe 2>nul
echo [INFO] All services stopped.