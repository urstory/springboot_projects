package com.example.configdemo.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 내용.md 5.4절 예제 - 환경 설정 서비스
 * @Value 어노테이션을 통한 프로파일별 설정값 주입
 */
@Component
public class EnvironmentService {

    private final Environment environment;

    @Value("${custom-message:기본 메시지}")
    private String customMessage;

    @Value("${app.environment:unknown}")
    private String appEnvironment;

    @Value("${app.debug-mode:false}")
    private boolean debugMode;

    @Value("${app.cache-enabled:true}")
    private boolean cacheEnabled;

    @Value("${server.port:8080}")
    private int serverPort;

    public EnvironmentService(Environment environment) {
        this.environment = environment;
    }

    /**
     * 커스텀 메시지 반환 (5.4절 예제)
     */
    public String getCustomMessage() {
        return customMessage;
    }

    /**
     * 현재 활성화된 프로파일 정보 반환
     */
    public String[] getActiveProfiles() {
        return environment.getActiveProfiles();
    }

    /**
     * 기본 프로파일 정보 반환
     */
    public String[] getDefaultProfiles() {
        return environment.getDefaultProfiles();
    }

    /**
     * 환경 정보 요약 반환
     */
    public String getEnvironmentSummary() {
        return String.format(
            "Environment Summary:\n" +
            "- Active Profiles: %s\n" +
            "- App Environment: %s\n" +
            "- Server Port: %d\n" +
            "- Debug Mode: %s\n" +
            "- Cache Enabled: %s\n" +
            "- Custom Message: %s",
            Arrays.toString(getActiveProfiles()),
            appEnvironment,
            serverPort,
            debugMode,
            cacheEnabled,
            customMessage
        );
    }

    /**
     * 특정 프로퍼티 값 조회
     */
    public String getProperty(String key) {
        return environment.getProperty(key);
    }

    /**
     * 특정 프로퍼티 값 조회 (기본값 포함)
     */
    public String getProperty(String key, String defaultValue) {
        return environment.getProperty(key, defaultValue);
    }

    /**
     * 프로파일 활성화 여부 확인
     */
    public boolean isProfileActive(String profile) {
        return Arrays.asList(getActiveProfiles()).contains(profile);
    }

    /**
     * 현재 환경이 로컬 개발 환경인지 확인
     */
    public boolean isLocalEnvironment() {
        return isProfileActive("local");
    }

    /**
     * 현재 환경이 운영 환경인지 확인
     */
    public boolean isProductionEnvironment() {
        return isProfileActive("prod") || isProfileActive("production");
    }

    // Getters
    public String getAppEnvironment() { return appEnvironment; }
    public boolean isDebugMode() { return debugMode; }
    public boolean isCacheEnabled() { return cacheEnabled; }
    public int getServerPort() { return serverPort; }
} 