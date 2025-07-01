package com.example.securityexam03.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.securityexam03.service.JwtBlacklistService;
import com.example.securityexam03.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 관리자 전용 JWT 관리 컨트롤러
 * 내용.md 6.6절 기반으로 구현
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private JwtBlacklistService jwtBlacklistService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 관리자 정보 조회
     */
    @GetMapping("/info")
    public ResponseEntity<?> getAdminInfo() {
        try {
            Map<String, Object> adminInfo = new HashMap<>();
            adminInfo.put("application", "Security Exam 03 - JWT Example");
            adminInfo.put("version", "1.0.0");
            adminInfo.put("description", "JWT 토큰 기반 인증 시스템 예제");
            adminInfo.put("features", new String[]{
                "JWT 토큰 생성/검증",
                "토큰 블랙리스트 관리",
                "사용자 권한 기반 접근 제어",
                "Redis 기반 토큰 무효화"
            });
            adminInfo.put("endpoints", new String[]{
                "POST /api/auth/login - 로그인",
                "POST /api/auth/logout - 로그아웃",
                "GET /api/auth/me - 현재 사용자 정보",
                "GET /api/auth/validate - 토큰 검증",
                "GET /api/admin/info - 관리자 정보",
                "POST /api/admin/blacklist/{tokenId} - 토큰 무효화",
                "DELETE /api/admin/blacklist/clear - 블랙리스트 초기화"
            });
            adminInfo.put("timestamp", new Date());
            adminInfo.put("blacklistCount", jwtBlacklistService.getBlacklistCount());

            return ResponseEntity.ok(adminInfo);
        } catch (Exception e) {
            logger.error("❌ 관리자 정보 조회 중 오류 발생: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Admin info retrieval failed");
            errorResponse.put("message", "관리자 정보 조회 중 오류가 발생했습니다");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 특정 토큰을 블랙리스트에 추가 (강제 무효화)
     */
    @PostMapping("/blacklist/token")
    public ResponseEntity<?> blacklistToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        
        if (token == null || token.trim().isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid token");
            errorResponse.put("message", "토큰이 제공되지 않았습니다");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try {
            // 토큰 유효성 검증
            if (!jwtUtil.validateToken(token)) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid token");
                errorResponse.put("message", "유효하지 않은 토큰입니다");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            // 토큰 만료 시간 조회
            Date expirationDate = jwtUtil.getExpirationDate(token);
            if (expirationDate != null) {
                long remainingTime = expirationDate.getTime() - System.currentTimeMillis();
                if (remainingTime > 0) {
                    jwtBlacklistService.blacklistToken(token, remainingTime);
                    
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "토큰이 블랙리스트에 추가되었습니다");
                    response.put("token", token.substring(0, Math.min(token.length(), 20)) + "...");
                    response.put("expiresAt", expirationDate);
                    response.put("timestamp", new Date());
                    
                    logger.info("✅ 관리자가 토큰을 블랙리스트에 추가: {}", token.substring(0, Math.min(token.length(), 20)) + "...");
                    return ResponseEntity.ok(response);
                } else {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("error", "Token already expired");
                    errorResponse.put("message", "이미 만료된 토큰입니다");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
                }
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Cannot determine token expiration");
                errorResponse.put("message", "토큰 만료 시간을 확인할 수 없습니다");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
        } catch (Exception e) {
            logger.error("❌ 토큰 블랙리스트 추가 중 오류 발생: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Blacklist operation failed");
            errorResponse.put("message", "토큰 블랙리스트 추가 중 오류가 발생했습니다");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 현재 사용자의 토큰을 강제 무효화
     */
    @PostMapping("/blacklist/current")
    public ResponseEntity<?> blacklistCurrentToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            
            try {
                Date expirationDate = jwtUtil.getExpirationDate(token);
                if (expirationDate != null) {
                    long remainingTime = expirationDate.getTime() - System.currentTimeMillis();
                    if (remainingTime > 0) {
                        jwtBlacklistService.blacklistToken(token, remainingTime);
                        
                        Map<String, Object> response = new HashMap<>();
                        response.put("message", "현재 토큰이 블랙리스트에 추가되었습니다");
                        response.put("timestamp", new Date());
                        
                        logger.info("✅ 관리자가 현재 토큰을 블랙리스트에 추가");
                        return ResponseEntity.ok(response);
                    }
                }
            } catch (Exception e) {
                logger.error("❌ 현재 토큰 블랙리스트 추가 중 오류 발생: {}", e.getMessage());
            }
        }

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Invalid token");
        errorResponse.put("message", "유효한 토큰을 찾을 수 없습니다");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * 블랙리스트 통계 조회
     */
    @GetMapping("/blacklist/stats")
    public ResponseEntity<?> getBlacklistStats() {
        try {
            long blacklistCount = jwtBlacklistService.getBlacklistCount();
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("blacklistCount", blacklistCount);
            stats.put("message", blacklistCount > 0 ? 
                "현재 " + blacklistCount + "개의 토큰이 블랙리스트에 있습니다" : 
                "블랙리스트가 비어있습니다");
            stats.put("timestamp", new Date());
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("❌ 블랙리스트 통계 조회 중 오류 발생: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Stats retrieval failed");
            errorResponse.put("message", "통계 조회 중 오류가 발생했습니다");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 블랙리스트 전체 초기화
     */
    @DeleteMapping("/blacklist/clear")
    public ResponseEntity<?> clearBlacklist() {
        try {
            long countBefore = jwtBlacklistService.getBlacklistCount();
            jwtBlacklistService.clearBlacklist();
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "블랙리스트가 초기화되었습니다");
            response.put("clearedCount", countBefore);
            response.put("timestamp", new Date());
            
            logger.info("✅ 관리자가 블랙리스트를 초기화 - {} 개의 토큰 삭제", countBefore);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("❌ 블랙리스트 초기화 중 오류 발생: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Blacklist clear failed");
            errorResponse.put("message", "블랙리스트 초기화 중 오류가 발생했습니다");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
} 