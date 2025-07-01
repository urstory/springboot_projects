#!/bin/bash

echo "================================================"
echo "Configuration Demo - Environment Variables Test"
echo "================================================"
echo

echo "Setting environment variables for database configuration..."
export DB_HOST=mydbserver.example.com
export DB_PORT=3307
export DB_NAME=production_db
export DB_USER=prod_user
export DB_PASS=super_secret_password

echo
echo "Environment variables set:"
echo "- DB_HOST=$DB_HOST"
echo "- DB_PORT=$DB_PORT"
echo "- DB_NAME=$DB_NAME"
echo "- DB_USER=$DB_USER"
echo "- DB_PASS=******* (masked)"
echo

echo "Building the application..."
./gradlew clean build -x test

if [ $? -ne 0 ]; then
    echo "Build failed!"
    exit 1
fi

echo
echo "Starting application with environment variables..."
echo "Server will run on port 8080 (local profile)"
echo
echo "Test database configuration with environment variables:"
echo "- GET http://localhost:8080/api/config/database-config"
echo

java -jar build/libs/configdemo-0.0.1-SNAPSHOT.jar --spring.profiles.active=local 