@echo off
echo ====================================
echo Configuration Demo - PROD Profile
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
echo Starting application with PROD profile...
echo Server will run on port 80 (requires admin privileges)
echo Custom message: "운영 환경입니다."
echo.
echo Test endpoints:
echo - Summary: http://localhost/api/config/summary
echo - Profile Message: http://localhost/api/config/profile-message
echo - Environment: http://localhost/api/config/environment
echo.
echo Note: Running on port 80 may require administrator privileges
echo.

java -jar build\libs\configdemo-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod

pause 