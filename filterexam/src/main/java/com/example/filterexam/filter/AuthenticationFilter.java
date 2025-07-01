package com.example.filterexam.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

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
 * API 인증을 위한 Filter
 * 커스텀 토큰 기반 인증 처리
 */
@Slf4j
@Component
@Order(4)  // CorsFilter 다음에 실행
public class AuthenticationFilter implements Filter {

    // 인증이 필요 없는 경로들
    private final List<String> excludedPaths = Arrays.asList(
            "/public", "/health", "/actuator", "/login", "/error", "/favicon.ico"
    );

    // 테스트용 유효한 토큰들
    private final List<String> validTokens = Arrays.asList(
            "test-token-123", "admin-token-456", "user-token-789"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("🔵 AuthenticationFilter 초기화됨");
        log.info("🔒 인증 제외 경로: {}", excludedPaths);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();

        // 인증 제외 경로 체크
        if (isExcludedPath(requestURI)) {
            log.debug("🔓 인증 제외 경로: {}", requestURI);
            chain.doFilter(request, response);
            return;
        }

        // 인증 토큰 검증
        if (authenticateRequest(httpRequest)) {
            log.debug("🔐 인증 성공: {}", requestURI);
            chain.doFilter(request, response);
        } else {
            log.warn("🚫 인증 실패: {}", requestURI);
            sendUnauthorizedResponse(httpResponse);
        }
    }

    private boolean isExcludedPath(String requestURI) {
        return excludedPaths.stream()
                .anyMatch(requestURI::startsWith);
    }

    private boolean authenticateRequest(HttpServletRequest request) {
        // Authorization 헤더에서 토큰 추출
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null) {
            // Bearer 토큰 방식
            if (authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                return isValidToken(token);
            }
            
            // Basic 인증 방식
            if (authHeader.startsWith("Basic ")) {
                return validateBasicAuth(authHeader.substring(6));
            }
        }

        // X-Auth-Token 헤더 체크
        String customToken = request.getHeader("X-Auth-Token");
        if (customToken != null) {
            return isValidToken(customToken);
        }

        // 쿠키에서 토큰 체크
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("auth-token".equals(cookie.getName())) {
                    return isValidToken(cookie.getValue());
                }
            }
        }

        return false;
    }

    private boolean isValidToken(String token) {
        boolean isValid = validTokens.contains(token);
        log.debug("토큰 검증: {} -> {}", token, isValid ? "유효" : "무효");
        return isValid;
    }

    private boolean validateBasicAuth(String base64Credentials) {
        try {
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            String[] parts = credentials.split(":", 2);
            
            if (parts.length == 2) {
                String username = parts[0];
                String password = parts[1];
                
                // 간단한 사용자 검증 (실제로는 DB나 외부 서비스 연동)
                boolean isValid = ("admin".equals(username) && "password".equals(password)) ||
                                ("user".equals(username) && "userpass".equals(password));
                
                log.debug("Basic 인증 검증: {} -> {}", username, isValid ? "성공" : "실패");
                return isValid;
            }
        } catch (Exception e) {
            log.error("Basic 인증 파싱 오류: {}", e.getMessage());
        }
        
        return false;
    }

    private void sendUnauthorizedResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        
        String jsonResponse = """
            {
                "success": false,
                "message": "인증이 필요합니다",
                "error": "UNAUTHORIZED",
                "timestamp": "%s",
                "supportedMethods": [
                    "Bearer 토큰: Authorization: Bearer <token>",
                    "커스텀 헤더: X-Auth-Token: <token>",
                    "Basic 인증: Authorization: Basic <base64(username:password)>",
                    "쿠키: auth-token=<token>"
                ],
                "validTokens": [
                    "test-token-123",
                    "admin-token-456", 
                    "user-token-789"
                ]
            }
            """.formatted(java.time.LocalDateTime.now());
        
        response.getWriter().write(jsonResponse);
    }

    @Override
    public void destroy() {
        log.info("🔴 AuthenticationFilter 소멸됨");
    }
} 