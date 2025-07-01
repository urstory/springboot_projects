#!/bin/bash

echo "==============================================="
echo "MySpringApp2 - CRUD API 애플리케이션 실행"
echo "==============================================="
echo

echo "[정보] Spring Boot 애플리케이션을 시작합니다..."
echo "[정보] 포트: 8080"
echo "[정보] API 테스트: http://localhost:8080/users"
echo

echo "[실행] ./gradlew bootRun"
./gradlew bootRun

if [ $? -ne 0 ]; then
    echo
    echo "[오류] 애플리케이션 실행 중 오류가 발생했습니다."
    exit 1
fi

echo
echo "[완료] 애플리케이션이 종료되었습니다." 