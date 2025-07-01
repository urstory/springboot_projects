package com.example.filterexam.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
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
 * CORS (Cross-Origin Resource Sharing) 처리를 위한 Filter
 * 브라우저의 CORS 정책을 처리
 */
@Slf4j
@Component
@Order(3)  // PerformanceFilter 다음에 실행
public class CorsFilter implements Filter {

    @Value("${app.filter.cors.allowed-origins:*}")
    private String allowedOrigins;

    @Value("${app.filter.cors.enabled:true}")
    private boolean corsEnabled;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("🔵 CorsFilter 초기화됨 - CORS 활성화: {}, 허용 오리진: {}", 
                corsEnabled, allowedOrigins);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (corsEnabled) {
            // CORS 헤더 설정
            addCorsHeaders(httpRequest, httpResponse);

            // Preflight 요청 처리
            if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
                log.debug("🔄 CORS Preflight 요청 처리: {}", httpRequest.getRequestURI());
                httpResponse.setStatus(HttpServletResponse.SC_OK);
                return; // OPTIONS 요청은 여기서 종료
            }
        }

        // 다음 필터 또는 컨트롤러로 요청 전달
        chain.doFilter(request, response);
    }

    private void addCorsHeaders(HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        
        // 허용된 오리진 체크
        if (isAllowedOrigin(origin)) {
            response.setHeader("Access-Control-Allow-Origin", origin != null ? origin : "*");
            log.debug("🌐 CORS 오리진 허용: {}", origin);
        } else {
            log.warn("🚫 CORS 오리진 거부: {}", origin);
        }

        // CORS 기본 헤더 설정
        response.setHeader("Access-Control-Allow-Methods", 
                          "GET, POST, PUT, DELETE, OPTIONS, PATCH");
        response.setHeader("Access-Control-Allow-Headers", 
                          "Origin, X-Requested-With, Content-Type, Accept, Authorization, X-Auth-Token");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Max-Age", "3600");
        
        // 커스텀 헤더 노출
        response.setHeader("Access-Control-Expose-Headers", 
                          "X-Request-ID, X-Processing-Time");
    }

    private boolean isAllowedOrigin(String origin) {
        if (origin == null) {
            return true; // Same-origin 요청
        }

        if ("*".equals(allowedOrigins)) {
            return true; // 모든 오리진 허용
        }

        // 설정된 오리진 목록과 비교
        String[] allowedOriginArray = allowedOrigins.split(",");
        for (String allowedOrigin : allowedOriginArray) {
            if (allowedOrigin.trim().equals(origin)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void destroy() {
        log.info("🔴 CorsFilter 소멸됨");
    }
} 