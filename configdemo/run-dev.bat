@echo off
echo ====================================
echo Configuration Demo - DEV Profile
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
echo Starting application with DEV profile...
echo Server will run on port 8081
echo Custom message: "개발 환경입니다."
echo.
echo Test endpoints:
echo - Summary: http://localhost:8081/api/config/summary
echo - Profile Message: http://localhost:8081/api/config/profile-message
echo - Environment: http://localhost:8081/api/config/environment
echo.

java -jar build\libs\configdemo-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

pause 