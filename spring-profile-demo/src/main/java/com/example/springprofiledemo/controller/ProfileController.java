package com.example.springprofiledemo.controller;

import com.example.springprofiledemo.config.AppConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProfileController {
    
    private final AppConfig appConfig;
    private final Environment environment;
    
    @GetMapping("/profile")
    public Map<String, Object> getProfileInfo() {
        Map<String, Object> result = new HashMap<>();
        
        // 활성화된 프로파일 정보
        String[] activeProfiles = environment.getActiveProfiles();
        result.put("activeProfiles", activeProfiles.length > 0 ? activeProfiles : new String[]{"default"});
        
        // 애플리케이션 설정 정보
        result.put("appConfig", appConfig);
        
        // 서버 포트 정보
        result.put("serverPort", environment.getProperty("server.port", "8080"));
        
        // 환경 변수 정보 (일부)
        result.put("javaVersion", System.getProperty("java.version"));
        result.put("osName", System.getProperty("os.name"));
        
        return result;
    }
    
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> result = new HashMap<>();
        result.put("status", "UP");
        result.put("environment", appConfig.getEnvironment());
        result.put("version", appConfig.getVersion());
        return result;
    }
} 