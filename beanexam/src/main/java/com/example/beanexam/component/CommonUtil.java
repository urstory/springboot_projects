package com.example.beanexam.component;

import java.util.UUID;

import org.springframework.stereotype.Component;

/**
 * @Component 방식으로 등록되는 공통 유틸리티 클래스
 * 내용.md 5.1절 예제
 * 
 * @Component는 특정한 역할(Service, Repository)이 명확하지 않은 
 * 공통 클래스에 주로 사용합니다.
 */
@Component
public class CommonUtil {

    /**
     * UUID 생성 메서드
     */
    public String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 문자열 유효성 검사
     */
    public boolean isValidString(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * 현재 시간을 밀리초로 반환
     */
    public long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 배열의 합계 계산
     */
    public int sum(int... numbers) {
        int total = 0;
        for (int num : numbers) {
            total += num;
        }
        return total;
    }
} 