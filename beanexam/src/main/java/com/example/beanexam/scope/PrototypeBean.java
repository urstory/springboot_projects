package com.example.beanexam.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Prototype Scope Bean 예제
 * 내용.md 5.2절 예제
 * 
 * Prototype Scope는 Bean을 요청할 때마다 새로운 인스턴스를 생성합니다.
 * 상태(state)를 가진 객체에 적합합니다.
 */
@Component
@Scope("prototype")
public class PrototypeBean {
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
        return "PrototypeBean instance - hashCode: " + this.hashCode() + ", count: " + count;
    }

    /**
     * 빈 생성 시간 추가
     */
    private final long createdTime = System.currentTimeMillis();

    public long getCreatedTime() {
        return createdTime;
    }
} 