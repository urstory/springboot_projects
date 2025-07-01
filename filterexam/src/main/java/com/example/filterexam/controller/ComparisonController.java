package com.example.filterexam.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.filterexam.dto.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Filter vs Interceptor 비교를 위한 Controller
 * 차이점을 실제 로그를 통해 확인할 수 있는 엔드포인트 제공
 */
@Slf4j
@RestController
@RequestMapping("/comparison")
public class ComparisonController {
    
    /**
     * Filter와 Interceptor 실행 순서 확인
     */
    @GetMapping("/execution-order")
    public ApiResponse<Map<String, Object>> executionOrderTest(HttpServletRequest request) {
        log.info("🔄 Filter vs Interceptor 실행 순서 테스트");
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Filter와 Interceptor의 실행 순서를 로그에서 확인하세요");
        data.put("expectedOrder", Map.of(
            "1", "Filter.doFilter() 시작 (LoggingFilter, PerformanceFilter, CorsFilter, AuthenticationFilter)",
            "2", "Interceptor.preHandle() (LoggingInterceptor, AuthInterceptor)",
            "3", "Controller 메서드 실행",
            "4", "Interceptor.postHandle() (역순)",
            "5", "Interceptor.afterCompletion() (역순)",
            "6", "Filter.doFilter() 완료 (역순)"
        ));
        data.put("timestamp", LocalDateTime.now());
        
        return ApiResponse.success("실행 순서 테스트 완료", data);
    }
    
    /**
     * Filter와 Interceptor의 역할 차이 설명
     */
    @GetMapping("/differences")
    public ApiResponse<Map<String, Object>> getDifferences() {
        log.info("📊 Filter vs Interceptor 차이점 설명");
        
        Map<String, Object> data = new HashMap<>();
        
        // Filter 특징
        Map<String, Object> filterFeatures = new HashMap<>();
        filterFeatures.put("실행시점", "Servlet Container 레벨 (Spring 이전)");
        filterFeatures.put("적용범위", "모든 요청 (정적 리소스 포함)");
        filterFeatures.put("예외처리", "Filter 체인에서 예외 발생 시 처리");
        filterFeatures.put("사용목적", "인증, CORS, 로깅, 성능측정, 인코딩 등");
        filterFeatures.put("Spring의존성", "Spring 컨텍스트 없이도 동작 가능");
        
        // Interceptor 특징
        Map<String, Object> interceptorFeatures = new HashMap<>();
        interceptorFeatures.put("실행시점", "Spring MVC 레벨 (DispatcherServlet 이후)");
        interceptorFeatures.put("적용범위", "Controller 요청만 (정적 리소스 제외)");
        interceptorFeatures.put("예외처리", "afterCompletion에서 예외 정보 확인 가능");
        interceptorFeatures.put("사용목적", "인가, 로그인 체크, Controller 공통 로직 등");
        interceptorFeatures.put("Spring의존성", "Spring 컨텍스트 필수 (Bean 주입 가능)");
        
        data.put("filter", filterFeatures);
        data.put("interceptor", interceptorFeatures);
        
        // 실행 순서 비교
        data.put("executionFlow", Map.of(
            "request", "Client -> Filter 체인 -> DispatcherServlet -> Interceptor -> Controller",
            "response", "Controller -> Interceptor -> DispatcherServlet -> Filter 체인 -> Client"
        ));
        
        return ApiResponse.success("Filter vs Interceptor 차이점 설명", data);
    }
    
    /**
     * 실제 사용 사례 예시
     */
    @GetMapping("/use-cases")
    public ApiResponse<Map<String, Object>> getUseCases() {
        log.info("💡 Filter vs Interceptor 사용 사례");
        
        Map<String, Object> data = new HashMap<>();
        
        data.put("filterUseCases", Map.of(
            "인증", "JWT 토큰 검증, API 키 확인",
            "CORS", "Cross-Origin 요청 처리",
            "로깅", "모든 HTTP 요청/응답 기록",
            "성능측정", "전체 요청 처리 시간 측정",
            "인코딩", "문자 인코딩 설정",
            "보안", "XSS, CSRF 방어"
        ));
        
        data.put("interceptorUseCases", Map.of(
            "인가", "사용자 권한 체크",
            "세션관리", "로그인 상태 확인",
            "감사로그", "비즈니스 로직 실행 기록",
            "캐싱", "Controller 결과 캐싱",
            "다국어", "Locale 설정",
            "테마", "사용자별 UI 테마 적용"
        ));
        
        return ApiResponse.success("사용 사례 설명 완료", data);
    }
    
    /**
     * 성능 비교 테스트
     */
    @GetMapping("/performance")
    public ApiResponse<Map<String, Object>> performanceComparison() throws InterruptedException {
        log.info("⚡ Filter vs Interceptor 성능 비교");
        
        long startTime = System.currentTimeMillis();
        
        // 의도적인 처리 시간
        Thread.sleep(100);
        
        long processingTime = System.currentTimeMillis() - startTime;
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Filter와 Interceptor의 시간 측정 방식 비교");
        data.put("controllerProcessingTime", processingTime + "ms");
        data.put("note", Map.of(
            "Filter", "전체 요청 처리 시간 (Servlet Container ~ Controller ~ Response)",
            "Interceptor", "Controller 실행 시간만 측정 가능"
        ));
        data.put("checkLogs", "PerformanceFilter와 LoggingInterceptor의 시간 측정 결과를 비교해보세요");
        
        return ApiResponse.success("성능 비교 테스트 완료", data);
    }
    
    /**
     * 예외 처리 방식 비교
     */
    @GetMapping("/exception-handling")
    public ApiResponse<Map<String, Object>> exceptionHandlingTest() {
        log.info("💥 예외 처리 방식 비교");
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Filter와 Interceptor의 예외 처리 방식 차이");
        data.put("filterExceptionHandling", "Filter 체인에서 예외 발생 시 catch 블록에서 처리");
        data.put("interceptorExceptionHandling", "afterCompletion 메서드에서 예외 정보 확인 가능");
        data.put("recommendation", "/comparison/trigger-exception 엔드포인트로 실제 예외 확인");
        
        return ApiResponse.success("예외 처리 방식 설명 완료", data);
    }
    
    /**
     * 예외 발생 트리거 (예외 처리 방식 확인용)
     */
    @GetMapping("/trigger-exception")
    public ApiResponse<Map<String, Object>> triggerException() {
        log.info("🔥 예외 발생 트리거");
        
        // 의도적으로 예외 발생
        throw new RuntimeException("Filter vs Interceptor 예외 처리 비교를 위한 테스트 예외");
    }
} 