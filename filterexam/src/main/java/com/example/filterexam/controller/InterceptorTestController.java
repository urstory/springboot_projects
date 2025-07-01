package com.example.filterexam.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.filterexam.dto.ApiResponse;
import com.example.filterexam.interceptor.AuthInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Interceptor 테스트를 위한 Controller
 * 다양한 Interceptor 동작을 확인할 수 있는 엔드포인트 제공
 */
@Slf4j
@RestController
@RequestMapping("/interceptor")
public class InterceptorTestController {
    
    /**
     * 기본 Interceptor 체인 테스트
     */
    @GetMapping("/test")
    public ApiResponse<Map<String, Object>> testInterceptors(HttpServletRequest request) {
        log.info("🎯 Interceptor 체인 테스트 시작");
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "모든 Interceptor가 정상적으로 동작했습니다");
        data.put("method", request.getMethod());
        data.put("uri", request.getRequestURI());
        data.put("timestamp", LocalDateTime.now());
        data.put("interceptorChain", "LoggingInterceptor -> AuthInterceptor");
        data.put("note", "이 메서드는 LoggingInterceptor의 preHandle, postHandle, afterCompletion을 모두 거칩니다");
        
        return ApiResponse.success("Interceptor 체인 테스트 성공", data);
    }
    
    /**
     * 인증이 필요한 엔드포인트 테스트
     */
    @AuthInterceptor.RequireAuth(roles = {"USER", "ADMIN"})
    @GetMapping("/auth/user")
    public ApiResponse<Map<String, Object>> userOnlyEndpoint(HttpServletRequest request) {
        log.info("🔐 사용자 전용 엔드포인트 접근");
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "USER 또는 ADMIN 권한으로 접근 가능한 엔드포인트");
        data.put("requiredRoles", new String[]{"USER", "ADMIN"});
        data.put("userAgent", request.getHeader("User-Agent"));
        data.put("authHeader", request.getHeader("Authorization"));
        
        return ApiResponse.success("사용자 전용 엔드포인트 접근 성공", data);
    }
    
    /**
     * 관리자 전용 엔드포인트 테스트
     */
    @AuthInterceptor.RequireAuth(roles = {"ADMIN"})
    @GetMapping("/auth/admin")
    public ApiResponse<Map<String, Object>> adminOnlyEndpoint(HttpServletRequest request) {
        log.info("🛡️ 관리자 전용 엔드포인트 접근");
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "ADMIN 권한만 접근 가능한 엔드포인트");
        data.put("requiredRoles", new String[]{"ADMIN"});
        data.put("timestamp", LocalDateTime.now());
        data.put("adminInfo", "관리자만 볼 수 있는 민감한 정보");
        
        return ApiResponse.success("관리자 전용 엔드포인트 접근 성공", data);
    }
    
    /**
     * 예외 발생 테스트 (afterCompletion에서 예외 로깅 확인)
     */
    @GetMapping("/error")
    public ApiResponse<Map<String, Object>> errorTest() {
        log.info("💥 의도적 예외 발생 테스트");
        
        // 의도적으로 예외 발생
        throw new RuntimeException("Interceptor 예외 처리 테스트용 RuntimeException");
    }
    
    /**
     * 느린 처리 테스트 (Interceptor의 시간 측정 확인)
     */
    @GetMapping("/slow")
    public ApiResponse<Map<String, Object>> slowProcessing(
            @RequestParam(defaultValue = "3000") long delay) throws InterruptedException {
        
        log.info("🐌 Interceptor 느린 처리 테스트 - 지연시간: {}ms", delay);
        
        // 의도적인 지연
        Thread.sleep(delay);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", String.format("Interceptor에서 %dms 지연 감지됨", delay));
        data.put("delay", delay);
        data.put("expectedWarning", "LoggingInterceptor에서 느린 요청 경고 출력 예상");
        
        return ApiResponse.success("느린 처리 테스트 완료", data);
    }
    
    /**
     * POST 요청 테스트 (요청 파라미터 로깅 확인)
     */
    @PostMapping("/data")
    public ApiResponse<Map<String, Object>> postDataTest(
            @RequestBody(required = false) Map<String, Object> requestData,
            @RequestParam(required = false) String param1,
            @RequestParam(required = false) String param2) {
        
        log.info("📝 POST 데이터 테스트");
        
        Map<String, Object> data = new HashMap<>();
        data.put("requestBody", requestData);
        data.put("queryParams", Map.of(
            "param1", param1 != null ? param1 : "null",
            "param2", param2 != null ? param2 : "null"
        ));
        data.put("message", "LoggingInterceptor에서 요청 파라미터가 로깅되어야 함");
        
        return ApiResponse.success("POST 데이터 테스트 완료", data);
    }
    
    /**
     * ModelAndView 테스트 (실제로는 REST API이므로 null이지만 로깅 확인용)
     */
    @GetMapping("/model")
    public ApiResponse<Map<String, Object>> modelTest() {
        log.info("📊 ModelAndView 테스트 (REST API)");
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "REST API에서는 ModelAndView가 null입니다");
        data.put("note", "LoggingInterceptor의 postHandle에서 null 확인 가능");
        
        return ApiResponse.success("ModelAndView 테스트 완료", data);
    }
    
    /**
     * 인터셉터 정보 조회
     */
    @GetMapping("/info")
    public ApiResponse<Map<String, Object>> getInterceptorInfo() {
        log.info("ℹ️ Interceptor 정보 조회");
        
        Map<String, Object> data = new HashMap<>();
        data.put("registeredInterceptors", Map.of(
            "LoggingInterceptor", "모든 요청의 처리 시간과 정보를 로깅",
            "AuthInterceptor", "@RequireAuth 어노테이션 기반 권한 체크"
        ));
        data.put("excludedPaths", new String[]{
            "/public/**", "/health", "/actuator/**", "/error", "/favicon.ico", "/filter/**"
        });
        data.put("authMethods", Map.of(
            "Authorization", "Bearer 토큰 또는 Basic 인증",
            "X-User-Role", "사용자 역할 직접 지정",
            "Cookie", "user-role 쿠키"
        ));
        
        return ApiResponse.success("Interceptor 정보 조회 완료", data);
    }
} 