#!/bin/bash

echo "==============================================="
echo "ViewResolver & MessageConverter 예제 실행"
echo "==============================================="
echo

echo "[정보] Spring Boot 애플리케이션을 시작합니다..."
echo "[정보] 포트: 8080"
echo "[정보] ViewResolver 예제: http://localhost:8080/pages/home"
echo "[정보] MessageConverter 예제: http://localhost:8080/api/info"
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