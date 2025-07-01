package com.example.configdemo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.configdemo.config.AppProperties;
import com.example.configdemo.config.DatabaseProperties;
import com.example.configdemo.service.EnvironmentService;

/**
 * 내용.md 5.4절 예제 - 프로파일 컨트롤러
 * 프로파일별 설정값을 확인할 수 있는 REST API 제공
 */
@RestController
@RequestMapping("/api/config")
public class ProfileController {

    private final EnvironmentService environmentService;
    private final AppProperties appProperties;
    private final DatabaseProperties databaseProperties;

    public ProfileController(EnvironmentService environmentService, 
                           AppProperties appProperties,
                           DatabaseProperties databaseProperties) {
        this.environmentService = environmentService;
        this.appProperties = appProperties;
        this.databaseProperties = databaseProperties;
    }

    /**
     * 5.4절 예제 - 프로파일별 커스텀 메시지 반환
     */
    @GetMapping("/profile-message")
    public String profileMessage() {
        return environmentService.getCustomMessage();
    }

    /**
     * 현재 활성화된 프로파일 정보 반환
     */
    @GetMapping("/profiles")
    public Map<String, Object> getProfiles() {
        Map<String, Object> result = new HashMap<>();
        result.put("activeProfiles", environmentService.getActiveProfiles());
        result.put("defaultProfiles", environmentService.getDefaultProfiles());
        result.put("isLocal", environmentService.isLocalEnvironment());
        result.put("isProduction", environmentService.isProductionEnvironment());
        return result;
    }

    /**
     * 환경 설정 요약 정보
     */
    @GetMapping("/environment")
    public Map<String, Object> getEnvironment() {
        Map<String, Object> result = new HashMap<>();
        result.put("summary", environmentService.getEnvironmentSummary());
        result.put("appEnvironment", environmentService.getAppEnvironment());
        result.put("serverPort", environmentService.getServerPort());
        result.put("debugMode", environmentService.isDebugMode());
        result.put("cacheEnabled", environmentService.isCacheEnabled());
        result.put("customMessage", environmentService.getCustomMessage());
        return result;
    }

    /**
     * 5.3절 예제 - 커스텀 애플리케이션 프로퍼티 정보
     */
    @GetMapping("/app-properties")
    public AppProperties getAppProperties() {
        return appProperties;
    }

    /**
     * 5.3절 예제 - 데이터베이스 설정 정보 (환경변수 활용)
     */
    @GetMapping("/database-config")
    public Map<String, Object> getDatabaseConfig() {
        Map<String, Object> result = new HashMap<>();
        result.put("connectionInfo", databaseProperties.getConnectionInfo());
        result.put("url", databaseProperties.getUrl());
        result.put("username", databaseProperties.getUsername());
        // 보안상 비밀번호는 마스킹하여 반환
        result.put("passwordMasked", "****");
        return result;
    }

    /**
     * 5.3절 예제 - List 타입 설정 확인 (서버 목록)
     */
    @GetMapping("/servers")
    public Map<String, Object> getServers() {
        Map<String, Object> result = new HashMap<>();
        result.put("servers", appProperties.getServers());
        result.put("serverCount", appProperties.getServers() != null ? appProperties.getServers().size() : 0);
        return result;
    }

    /**
     * 5.3절 예제 - Map 타입 설정 확인 (에러 코드)
     */
    @GetMapping("/error-codes")
    public Map<String, Object> getErrorCodes() {
        Map<String, Object> result = new HashMap<>();
        result.put("errorCodes", appProperties.getErrorCodes());
        result.put("codeCount", appProperties.getErrorCodes() != null ? appProperties.getErrorCodes().size() : 0);
        return result;
    }

    /**
     * 5.3절 예제 - 중첩 객체 설정 확인 (Timeout)
     */
    @GetMapping("/timeout-config")
    public Map<String, Object> getTimeoutConfig() {
        Map<String, Object> result = new HashMap<>();
        AppProperties.Timeout timeout = appProperties.getTimeout();
        if (timeout != null) {
            result.put("connection", timeout.getConnection());
            result.put("read", timeout.getRead());
            result.put("timeoutInfo", timeout.toString());
        }
        return result;
    }

    /**
     * 프로파일별 기능 설정 확인
     */
    @GetMapping("/features")
    public Map<String, Object> getFeatures() {
        Map<String, Object> result = new HashMap<>();
        AppProperties.Features features = appProperties.getFeatures();
        if (features != null) {
            result.put("mockExternalApi", features.isMockExternalApi());
            result.put("logAllRequests", features.isLogAllRequests());
            result.put("featuresInfo", features.toString());
        }
        return result;
    }

    /**
     * 전체 설정 정보 요약
     */
    @GetMapping("/summary")
    public Map<String, Object> getSummary() {
        Map<String, Object> result = new HashMap<>();
        result.put("title", "내용.md 5.3절, 5.4절 Configuration Demo 요약");
        result.put("activeProfiles", environmentService.getActiveProfiles());
        result.put("customMessage", environmentService.getCustomMessage());
        result.put("appProperties", appProperties);
        result.put("databaseInfo", databaseProperties.getConnectionInfo());
        result.put("endpoints", Map.of(
            "profileMessage", "/api/config/profile-message",
            "profiles", "/api/config/profiles", 
            "environment", "/api/config/environment",
            "appProperties", "/api/config/app-properties",
            "databaseConfig", "/api/config/database-config",
            "servers", "/api/config/servers",
            "errorCodes", "/api/config/error-codes",
            "timeoutConfig", "/api/config/timeout-config",
            "features", "/api/config/features"
        ));
        return result;
    }
} 