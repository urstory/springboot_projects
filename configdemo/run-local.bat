@echo off
echo ====================================
echo Configuration Demo - LOCAL Profile
echo ====================================
echo.

echo Building the application...
call .\gradlew.bat clean build -x test

if %ERRORLEVEL% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)

echo.
echo Starting application with LOCAL profile...
echo Server will run on port 8080
echo Custom message: "Local 환경입니다."
echo.
echo Test endpoints:
echo - Summary: http://localhost:8080/api/config/summary
echo - Profile Message: http://localhost:8080/api/config/profile-message
echo - Environment: http://localhost:8080/api/config/environment
echo.

java -jar build\libs\configdemo-0.0.1-SNAPSHOT.jar --spring.profiles.active=local

pause 