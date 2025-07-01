package com.example.securityexam03.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 애플리케이션 정보 컨트롤러
 */
@RestController
@RequestMapping("/api")
public class InfoController {

    /**
     * 애플리케이션 정보 조회 (공개 엔드포인트)
     */
    @GetMapping("/info")
    public ResponseEntity<?> getInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("application", "Security Exam 03 - JWT Example");
        info.put("version", "1.0.0");
        info.put("description", "JWT(Json Web Token) 기반 인증 시스템 예제");
        info.put("port", 8082);
        info.put("features", new String[]{
            "JWT 토큰 기반 인증",
            "토큰 블랙리스트 관리",
            "사용자 권한 기반 접근 제어",
            "Redis 기반 토큰 무효화",
            "보안 모범 사례 적용"
        });
        info.put("endpoints", new String[]{
            "GET /api/info - 애플리케이션 정보 (공개)",
            "POST /api/auth/login - JWT 로그인",
            "POST /api/auth/logout - JWT 로그아웃",
            "GET /api/auth/me - 현재 사용자 정보",
            "GET /api/auth/validate - 토큰 유효성 검증",
            "GET /api/auth/users - 테스트 사용자 목록",
            "GET /api/admin/info - 관리자 정보 (ADMIN 권한 필요)",
            "POST /api/admin/blacklist/token - 토큰 강제 무효화 (ADMIN)",
            "GET /api/admin/blacklist/stats - 블랙리스트 통계 (ADMIN)",
            "DELETE /api/admin/blacklist/clear - 블랙리스트 초기화 (ADMIN)"
        });
        info.put("security", new String[]{
            "JWT 토큰 만료 시간: 1시간",
            "블랙리스트 기반 토큰 무효화",
            "HTTPS 전송 권장",
            "CSRF 보호 비활성화 (JWT 사용)"
        });
        info.put("testUsers", new String[]{
            "user1/password (ROLE_USER)",
            "admin/password (ROLE_ADMIN, ROLE_USER)",
            "guest/password (ROLE_GUEST)",
            "jwt_user/password (ROLE_USER)",
            "jwt_admin/admin123 (ROLE_ADMIN, ROLE_USER)"
        });
        info.put("timestamp", new Date());

        return ResponseEntity.ok(info);
    }

    /**
     * 헬스 체크 엔드포인트
     */
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", new Date());
        health.put("uptime", System.currentTimeMillis());
        
        return ResponseEntity.ok(health);
    }
} 