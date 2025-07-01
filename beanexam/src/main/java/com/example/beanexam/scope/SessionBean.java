package com.example.beanexam.scope;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Session Scope Bean 예제 (웹 환경 전용)
 * 내용.md 5.2절 예제
 * 
 * HTTP 세션당 하나의 Bean 인스턴스를 생성하고 유지합니다.
 * 로그인 정보나 장바구니 데이터처럼 세션 동안 유지해야 하는 정보를 관리할 때 유용합니다.
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionBean {
    private List<String> cart = new ArrayList<>();
    private final long sessionStartTime = System.currentTimeMillis();
    private String username = "anonymous";

    public void addItem(String item) {
        cart.add(item);
    }

    public void removeItem(String item) {
        cart.remove(item);
    }

    public List<String> getCart() {
        return new ArrayList<>(cart);
    }

    public int getCartSize() {
        return cart.size();
    }

    public void clearCart() {
        cart.clear();
    }

    public long getSessionStartTime() {
        return sessionStartTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 세션 정보 반환
     */
    public String getSessionInfo() {
        return String.format("SessionBean[hashCode=%d, username=%s, cartSize=%d, sessionStart=%d]", 
                           this.hashCode(), username, cart.size(), sessionStartTime);
    }
} 