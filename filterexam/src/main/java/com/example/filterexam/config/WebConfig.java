package com.example.filterexam.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.filterexam.interceptor.AuthInterceptor;
import com.example.filterexam.interceptor.LoggingInterceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Web MVC 설정
 * Interceptor 등록 및 웹 계층 설정
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    
    private final LoggingInterceptor loggingInterceptor;
    private final AuthInterceptor authInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("🔧 Interceptor 등록 시작");
        
        // LoggingInterceptor 등록 (모든 경로)
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/actuator/**", "/error", "/favicon.ico")
                .order(1);
        
        log.info("✅ LoggingInterceptor 등록됨 - 모든 경로");
        
        // AuthInterceptor 등록 (특정 경로 제외)
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/public/**",
                        "/health",
                        "/actuator/**",
                        "/error",
                        "/favicon.ico",
                        "/api/public/**",
                        "/filter/**"  // Filter 테스트 경로는 인터셉터에서 제외
                )
                .order(2);
        
        log.info("✅ AuthInterceptor 등록됨 - 제한된 경로");
        
        log.info("🔧 Interceptor 등록 완료");
    }
} 