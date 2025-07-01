package com.example.beanexam.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.example.beanexam.model.MyEncoder;
import com.example.beanexam.model.MyService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Configuration 어노테이션을 사용한 Bean 정의 예시
 * @Bean 메서드를 통해 수동으로 Bean을 정의하는 방법
 */
@Slf4j
@Configuration
public class AppConfig {
    
    /**
     * DateTimeFormatter Bean 정의
     */
    @Bean
    public DateTimeFormatter dateTimeFormatter() {
        log.info("🔧 DateTimeFormatter Bean을 생성합니다.");
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }
    
    /**
     * 기본 설정 맵 Bean 정의
     */
    @Bean
    public Map<String, String> applicationSettings() {
        log.info("🔧 applicationSettings Bean을 생성합니다.");
        Map<String, String> settings = new HashMap<>();
        settings.put("app.name", "Bean Example Application");
        settings.put("app.version", "1.0.0");
        settings.put("app.environment", "development");
        settings.put("app.created.at", LocalDateTime.now().toString());
        return settings;
    }
    
    /**
     * 주요(Primary) 설정 서비스 Bean 정의
     */
    @Primary
    @Bean
    public ConfigurationService primaryConfigurationService() {
        log.info("🔧 Primary ConfigurationService Bean을 생성합니다.");
        return new ConfigurationService("PRIMARY");
    }
    
    /**
     * 보조 설정 서비스 Bean 정의
     */
    @Bean
    public ConfigurationService secondaryConfigurationService() {
        log.info("🔧 Secondary ConfigurationService Bean을 생성합니다.");
        return new ConfigurationService("SECONDARY");
    }

    /**
     * MyService Bean 정의 (내용.md 5.1절 예제)
     * @Bean 방식으로 객체를 생성하고 등록하는 예
     */
    @Bean
    public MyService myService() {
        log.info("🔧 MyService Bean을 생성합니다.");
        // 생성자 주입 방식으로 객체를 생성하는 예
        return new MyService("Hello, Spring Bean from 내용.md!");
    }

    /**
     * MyEncoder Bean 정의 (내용.md 5.1절 예제)
     * 사용자 정의 알고리즘 이름 주입
     */
    @Bean
    public MyEncoder myEncoder() {
        log.info("🔧 MyEncoder Bean을 생성합니다.");
        // 사용자 정의 알고리즘 이름 주입
        return new MyEncoder("SHA-256");
    }
    
    /**
     * 설정 서비스를 나타내는 클래스
     */
    public static class ConfigurationService {
        private final String type;
        private final LocalDateTime createdAt;
        
        public ConfigurationService(String type) {
            this.type = type;
            this.createdAt = LocalDateTime.now();
            log.info("🔧 ConfigurationService 생성 - 타입: {}", type);
        }
        
        public String getServiceInfo() {
            return String.format("ConfigurationService - 타입: %s, 생성시간: %s, 해시코드: %d",
                               type, createdAt, this.hashCode());
        }
        
        public String getType() { return type; }
        public LocalDateTime getCreatedAt() { return createdAt; }
    }
} 