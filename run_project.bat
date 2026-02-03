@echo off
call gradlew.bat build

start "Spring Boot Server" cmd /k "gradlew.bat :rate-limited-api:run"

timeout /t 5

start "Request Simulator" cmd /k "gradlew.bat :request-simulator:run"

pause