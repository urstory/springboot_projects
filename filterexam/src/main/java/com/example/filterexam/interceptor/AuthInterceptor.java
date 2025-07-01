package com.example.filterexam.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 인가(Authorization) 처리를 위한 Interceptor
 * 컨트롤러 메서드별 권한 체크
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    // 관리자 권한이 필요한 경로들
    private final List<String> adminPaths = Arrays.asList(
            "/admin", "/management", "/system"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // HandlerMethod가 아닌 경우 (정적 리소스 등) 통과
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        String requestURI = request.getRequestURI();
        
        // @RequireAuth 어노테이션 체크
        if (handlerMethod.hasMethodAnnotation(RequireAuth.class)) {
            RequireAuth requireAuth = handlerMethod.getMethodAnnotation(RequireAuth.class);
            return checkPermission(request, response, requireAuth.roles(), requestURI);
        }

        // 관리자 경로 체크
        if (isAdminPath(requestURI)) {
            return checkPermission(request, response, new String[]{"ADMIN"}, requestURI);
        }

        // 기본적으로 허용
        return true;
    }

    private boolean isAdminPath(String requestURI) {
        return adminPaths.stream().anyMatch(requestURI::startsWith);
    }

    private boolean checkPermission(HttpServletRequest request, HttpServletResponse response,
                                   String[] requiredRoles, String requestURI) throws Exception {
        
        // 현재 사용자 정보 추출 (실제로는 SecurityContext나 JWT에서 가져옴)
        String userRole = extractUserRole(request);
        
        if (userRole == null) {
            log.warn("🚫 [AuthInterceptor] 사용자 정보 없음: {}", requestURI);
            sendForbiddenResponse(response, "사용자 정보가 없습니다");
            return false;
        }

        // 권한 체크
        boolean hasPermission = Arrays.asList(requiredRoles).contains(userRole);
        
        if (hasPermission) {
            log.info("✅ [AuthInterceptor] 권한 확인: {} - 사용자: {}, 권한: {}", 
                    requestURI, userRole, Arrays.toString(requiredRoles));
            return true;
        } else {
            log.warn("🚫 [AuthInterceptor] 권한 부족: {} - 사용자: {}, 필요권한: {}", 
                    requestURI, userRole, Arrays.toString(requiredRoles));
            sendForbiddenResponse(response, 
                    String.format("권한이 부족합니다. 필요 권한: %s, 현재 권한: %s", 
                            Arrays.toString(requiredRoles), userRole));
            return false;
        }
    }

    private String extractUserRole(HttpServletRequest request) {
        // Authorization 헤더에서 역할 추출 (간단한 예시)
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null) {
            if (authHeader.contains("admin-token")) {
                return "ADMIN";
            } else if (authHeader.contains("user-token")) {
                return "USER";
            } else if (authHeader.contains("test-token")) {
                return "USER";
            }
        }

        // X-User-Role 헤더 체크
        String roleHeader = request.getHeader("X-User-Role");
        if (roleHeader != null) {
            return roleHeader.toUpperCase();
        }

        // 쿠키에서 역할 추출
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("user-role".equals(cookie.getName())) {
                    return cookie.getValue().toUpperCase();
                }
            }
        }

        return null;
    }

    private void sendForbiddenResponse(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        
        String jsonResponse = """
            {
                "success": false,
                "message": "%s",
                "error": "FORBIDDEN",
                "timestamp": "%s",
                "hint": "X-User-Role 헤더나 적절한 토큰을 사용하여 권한을 설정하세요"
            }
            """.formatted(message, java.time.LocalDateTime.now());
        
        response.getWriter().write(jsonResponse);
    }

    /**
     * 권한 체크를 위한 커스텀 어노테이션
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequireAuth {
        String[] roles() default {"USER"};
    }
} 