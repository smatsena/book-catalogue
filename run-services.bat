@echo off
setlocal enabledelayedexpansion

echo ============================================
echo Building and Starting Book Catalogue Services
echo ============================================
echo.

REM Build Management Service
echo [1/4] Building Management Service...
cd /d %~dp0management-service
call mvn clean package -DskipTests
if errorlevel 1 (
    echo ERROR: Failed to build Management Service
    pause
    exit /b 1
)
echo Management Service built successfully.
echo.

REM Build Web Service
echo [2/4] Building Web Service...
cd /d %~dp0web-service
call mvn clean package -DskipTests
if errorlevel 1 (
    echo ERROR: Failed to build Web Service
    pause
    exit /b 1
)
echo Web Service built successfully.
echo.

REM Set JAR file paths
set MANAGEMENT_JAR=%~dp0management-service\target\management-service-1.0-SNAPSHOT.jar
set WEB_JAR=%~dp0web-service\target\web-service-1.0-SNAPSHOT.jar

REM Verify JAR files exist
if not exist "%MANAGEMENT_JAR%" (
    echo ERROR: Management Service JAR not found at %MANAGEMENT_JAR%
    pause
    exit /b 1
)

if not exist "%WEB_JAR%" (
    echo ERROR: Web Service JAR not found at %WEB_JAR%
    pause
    exit /b 1
)

echo [3/4] Starting Management Service on port 8081...
start "Management Service (Port 8081)" cmd /k "cd /d %~dp0 && java -jar "%MANAGEMENT_JAR%""

REM Wait a few seconds for the management service to start
echo Waiting for Management Service to initialize...
timeout /t 5 /nobreak >nul

echo [4/4] Starting Web Service on port 8082...
start "Web Service (Port 8082)" cmd /k "cd /d %~dp0 && java -jar "%WEB_JAR%""

echo.
echo ============================================
echo Both services are starting...
echo ============================================
echo Management Service: http://localhost:8081
echo Web Service: http://localhost:8082
echo.
echo Services are running in separate windows.
echo Close those windows or press Ctrl+C to stop them.
echo.
pause

