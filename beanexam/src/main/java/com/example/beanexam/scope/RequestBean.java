package com.example.beanexam.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Request Scope Bean 예제 (웹 환경 전용)
 * 내용.md 5.2절 예제
 * 
 * 웹 요청당 하나의 Bean 인스턴스가 생성됩니다.
 * HTTP 요청 처리 과정에서만 Bean이 존재하며, 요청이 끝나면 제거됩니다.
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestBean {
    private String data = "Request Data";
    private final long requestTime = System.currentTimeMillis();
    private int accessCount = 0;

    public String getData() {
        accessCount++;
        return data + " (accessed " + accessCount + " times)";
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public int getAccessCount() {
        return accessCount;
    }

    /**
     * 요청별 고유 정보 반환
     */
    public String getRequestInfo() {
        return String.format("RequestBean[hashCode=%d, requestTime=%d, accessCount=%d]", 
                           this.hashCode(), requestTime, accessCount);
    }
} 