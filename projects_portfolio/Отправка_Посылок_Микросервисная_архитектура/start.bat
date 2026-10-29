@echo off
chcp 65001 >nul
echo ========================================
echo   Запуск всех микросервисов 
echo ========================================

echo [1/5] Запуск gRPC-сервера (порт 8083, gRPC 9090)...
start "CALC-SERVER (8083)" cmd /k "cd /d %~dp0 && mvnw.cmd -pl logistic-finance-server spring-boot:run"
timeout /t 5 /nobreak >nul

echo [2/5] Запуск основного сервиса (порт 8080)...
start "LOGISTIC-SERVICE (8080)" cmd /k "cd /d %~dp0 && mvnw.cmd -pl service-impl-module spring-boot:run"
timeout /t 8 /nobreak >nul

echo [3/5] Запуск Enrichment Client (порт 8082)...
start "ENRICHMENT-CLIENT (8082)" cmd /k "cd /d %~dp0 && mvnw.cmd -pl logistic-enrichment-client spring-boot:run"
timeout /t 5 /nobreak >nul

echo [4/5] Запуск Audit Service (порт 8084)...
start "AUDIT-SERVICE (8084)" cmd /k "cd /d %~dp0 && mvnw.cmd -pl events-impl-module spring-boot:run"
timeout /t 5 /nobreak >nul

echo [5/5] Запуск Notification Service (порт 8085)...
start "NOTIFICATION-SERVICE (8085)" cmd /k "cd /d %~dp0 && mvnw.cmd -pl notification-service spring-boot:run"

echo.
echo ========================================
echo   Все 5 окон запущены
echo   Swagger: http://localhost:8080/swagger-ui.html
echo ========================================
pause