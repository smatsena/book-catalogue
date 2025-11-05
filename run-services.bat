:: This doesn't work, for some reason the web-service doesn't find the jsp.

@echo off
setlocal enabledelayedexpansion

:: ==== CONFIG ====
set MGMT_PORT=8081
set WEB_PORT=8082
set ROOT=%~dp0
set LOG_DIR=%ROOT%logs
if not exist "%LOG_DIR%" mkdir "%LOG_DIR%"

set MGMT_JAR=%ROOT%management-service\target\management-service-1.0-SNAPSHOT.jar
set WEB_JAR=%ROOT%web-service\target\web-service-1.0-SNAPSHOT.jar

echo Building services...
call mvn -q -DskipTests clean package
if errorlevel 1 exit /b 1

echo Starting Management Service on %MGMT_PORT%...
start "Management Service" cmd /k ^
 "java -Dserver.port=%MGMT_PORT% -jar "%MGMT_JAR%" >> "%LOG_DIR%\management.log" 2>&1"

echo Starting Web Service on %WEB_PORT%...
start "Web Service" cmd /k ^
 "java -Dserver.port=%WEB_PORT% -jar "%WEB_JAR%" >> "%LOG_DIR%\web.log" 2>&1"

echo Waiting for web-service to start...
powershell -Command ^
 "for($i=0;$i -lt 60;$i++){try{(Invoke-WebRequest http://localhost:%WEB_PORT%/actuator/health -UseBasicParsing)>$null;exit 0}catch{Start-Sleep 1}}exit 1"

if errorlevel 1 (
  echo Web service health check failed. Check logs.
) else (
  echo Opening browser...
  start "" "http://localhost:%WEB_PORT%/books"
)

echo Done.