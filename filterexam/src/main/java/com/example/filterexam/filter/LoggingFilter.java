package com.example.filterexam.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * HTTP 요청/응답 로깅을 위한 Filter
 * 모든 HTTP 요청의 상세 정보를 기록
 */
@Slf4j
@Component
@Order(1)  // 필터 실행 순서 (낮은 숫자가 먼저 실행)
public class LoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("🔵 LoggingFilter 초기화됨");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        long startTime = System.currentTimeMillis();
        
        // 요청 정보 로깅
        logRequestDetails(httpRequest, requestId);
        
        try {
            // 다음 필터 또는 컨트롤러로 요청 전달
            chain.doFilter(request, response);
        } finally {
            // 응답 정보 로깅
            long processingTime = System.currentTimeMillis() - startTime;
            logResponseDetails(httpRequest, httpResponse, requestId, processingTime);
        }
    }

    private void logRequestDetails(HttpServletRequest request, String requestId) {
        log.info("📥 [{}] 요청 시작 - {} {} from {}",
                requestId,
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr());
        
        log.debug("📋 [{}] 요청 헤더:", requestId);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            log.debug("   {}: {}", headerName, request.getHeader(headerName));
        }
        
        // 쿼리 파라미터 로깅
        if (request.getQueryString() != null) {
            log.debug("📋 [{}] 쿼리 파라미터: {}", requestId, request.getQueryString());
        }
        
        // 세션 정보 로깅
        if (request.getSession(false) != null) {
            log.debug("📋 [{}] 세션 ID: {}", requestId, request.getSession().getId());
        }
    }

    private void logResponseDetails(HttpServletRequest request, HttpServletResponse response, 
                                   String requestId, long processingTime) {
        log.info("📤 [{}] 요청 완료 - {} {} - 상태: {}, 처리시간: {}ms",
                requestId,
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                processingTime);
        
        // 느린 요청 경고
        if (processingTime > 1000) {
            log.warn("⚠️ [{}] 느린 요청 감지! 처리시간: {}ms", requestId, processingTime);
        }
    }

    @Override
    public void destroy() {
        log.info("🔴 LoggingFilter 소멸됨");
    }
} 