package com.example.filterexam.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.filterexam.dto.ApiResponse;
import com.example.filterexam.filter.PerformanceFilter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Filter 테스트를 위한 Controller
 * 다양한 Filter 동작을 확인할 수 있는 엔드포인트 제공
 */
@Slf4j
@RestController
@RequestMapping("/filter")
@RequiredArgsConstructor
public class FilterTestController {
    
    private final PerformanceFilter performanceFilter;
    
    /**
     * 기본 Filter 체인 테스트
     */
    @GetMapping("/test")
    public ApiResponse<Map<String, Object>> testFilters(HttpServletRequest request) {
        log.info("🎯 Filter 체인 테스트 시작");
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "모든 Filter가 정상적으로 동작했습니다");
        data.put("method", request.getMethod());
        data.put("uri", request.getRequestURI());
        data.put("timestamp", LocalDateTime.now());
        data.put("filterChain", "LoggingFilter -> PerformanceFilter -> CorsFilter -> AuthenticationFilter");
        
        return ApiResponse.success("Filter 체인 테스트 성공", data);
    }
    
    /**
     * 성능 측정 Filter 테스트 (의도적으로 느린 응답)
     */
    @GetMapping("/slow")
    public ApiResponse<Map<String, Object>> slowResponse(
            @RequestParam(defaultValue = "2000") long delay) throws InterruptedException {
        
        log.info("🐌 느린 응답 테스트 시작 - 지연시간: {}ms", delay);
        
        // 의도적인 지연
        Thread.sleep(delay);
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", String.format("의도적으로 %dms 지연된 응답", delay));
        data.put("delay", delay);
        data.put("expectedWarning", "PerformanceFilter에서 느린 요청 경고가 출력되어야 함");
        
        return ApiResponse.success("느린 응답 테스트 완료", data);
    }
    
    /**
     * CORS Filter 테스트
     */
    @GetMapping("/cors")
    public ApiResponse<Map<String, Object>> corsTest(HttpServletRequest request) {
        log.info("🌐 CORS 테스트 시작");
        
        Map<String, Object> data = new HashMap<>();
        data.put("message", "CORS 헤더가 응답에 포함되어야 함");
        data.put("origin", request.getHeader("Origin"));
        data.put("userAgent", request.getHeader("User-Agent"));
        data.put("expectedHeaders", Map.of(
            "Access-Control-Allow-Origin", "요청 Origin 또는 *",
            "Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH",
            "Access-Control-Allow-Headers", "다양한 허용 헤더들"
        ));
        
        return ApiResponse.success("CORS 테스트 완료", data);
    }
    
    /**
     * OPTIONS 요청 (CORS Preflight) 테스트
     */
    @RequestMapping(value = "/preflight", method = RequestMethod.OPTIONS)
    public void preflightTest() {
        log.info("✈️ CORS Preflight 요청 처리됨");
        // CorsFilter에서 처리되므로 이 메서드는 실행되지 않음
    }
    
    /**
     * 성능 통계 조회
     */
    @GetMapping("/performance")
    public ApiResponse<Map<String, Object>> getPerformanceStats() {
        log.info("📊 성능 통계 조회");
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRequests", performanceFilter.getTotalRequests());
        stats.put("averageResponseTime", performanceFilter.getAverageResponseTime());
        stats.put("endpointStats", performanceFilter.getPerformanceStats());
        
        return ApiResponse.success("성능 통계 조회 완료", stats);
    }
    
    /**
     * 다양한 HTTP 메서드 테스트
     */
    @PostMapping("/test")
    public ApiResponse<Map<String, Object>> postTest(@RequestBody(required = false) Map<String, Object> body) {
        Map<String, Object> data = new HashMap<>();
        data.put("method", "POST");
        data.put("body", body);
        data.put("message", "POST 요청 처리됨");
        
        return ApiResponse.success("POST 테스트 완료", data);
    }
    
    @PutMapping("/test")
    public ApiResponse<Map<String, Object>> putTest(@RequestBody(required = false) Map<String, Object> body) {
        Map<String, Object> data = new HashMap<>();
        data.put("method", "PUT");
        data.put("body", body);
        data.put("message", "PUT 요청 처리됨");
        
        return ApiResponse.success("PUT 테스트 완료", data);
    }
    
    @DeleteMapping("/test")
    public ApiResponse<Map<String, Object>> deleteTest() {
        Map<String, Object> data = new HashMap<>();
        data.put("method", "DELETE");
        data.put("message", "DELETE 요청 처리됨");
        
        return ApiResponse.success("DELETE 테스트 완료", data);
    }
} 