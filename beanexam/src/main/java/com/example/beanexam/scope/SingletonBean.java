package com.example.beanexam.scope;

import org.springframework.stereotype.Component;

/**
 * Singleton Scope Bean 예제 (기본값)
 * 내용.md 5.2절 예제
 * 
 * Singleton은 스프링 컨테이너당 단 하나의 Bean 인스턴스만 생성됩니다.
 * 모든 요청에서 동일한 Bean 인스턴스를 사용합니다.
 */
@Component // 기본적으로 Singleton Scope
public class SingletonBean {
    private int count = 0;

    public int incrementAndGet() {
        return ++count;
    }

    public int getCount() {
        return count;
    }

    public void reset() {
        count = 0;
    }

    /**
     * 빈 생성 시점 확인용 메서드
     */
    public String getInstanceInfo() {
        return "SingletonBean instance - hashCode: " + this.hashCode() + ", count: " + count;
    }
} 