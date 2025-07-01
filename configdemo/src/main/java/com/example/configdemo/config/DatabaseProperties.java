package com.example.configdemo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 내용.md 5.3절 예제 - 환경변수 활용 데이터베이스 설정
 * Placeholder 및 환경변수를 활용한 설정 매핑
 */
@Component
@ConfigurationProperties(prefix = "database")
public class DatabaseProperties {
    private String url;
    private String username;
    private String password;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 보안을 위해 비밀번호는 마스킹하여 출력
     */
    @Override
    public String toString() {
        return String.format("DatabaseProperties{url='%s', username='%s', password='%s'}", 
                           url, username, maskPassword(password));
    }

    private String maskPassword(String password) {
        if (password == null || password.isEmpty()) {
            return "****";
        }
        return "*".repeat(password.length());
    }

    /**
     * 실제 연결 정보 확인용 (로깅용)
     */
    public String getConnectionInfo() {
        return String.format("Database Connection - URL: %s, User: %s", url, username);
    }
} 