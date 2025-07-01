package com.example.securityexam01.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.securityexam01.dto.LoginRequest;
import com.example.securityexam01.service.CustomUserDetailsService;

/**
 * 인증 관련 API 컨트롤러
 * 내용.md 6.2절 - API 기반 로그인 처리 구현 실습 예제
 */
@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    public AuthenticationController(AuthenticationManager authenticationManager, 
                                  CustomUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    /**
     * 로그인 API
     * POST /api/login
     * 요청 Body: {"username": "...", "password": "..."}
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken token =
            new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());

        try {
            // AuthenticationManager를 통한 인증 수행
            Authentication authentication = authenticationManager.authenticate(token);
            
            // 인증 성공 시 SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            return ResponseEntity.ok(Map.of(
                "message", "로그인 성공",
                "username", authentication.getName(),
                "authorities", authentication.getAuthorities()
            ));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Map.of("message", "로그인 실패: 잘못된 사용자명 또는 비밀번호"));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Map.of("message", "로그인 실패: " + e.getMessage()));
        }
    }

    /**
     * 현재 인증된 사용자 정보 조회 API
     * GET /api/me
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Map.of("message", "인증되지 않은 사용자입니다."));
        }

        return ResponseEntity.ok(Map.of(
            "username", authentication.getName(),
            "authorities", authentication.getAuthorities(),
            "authenticated", authentication.isAuthenticated()
        ));
    }

    /**
     * 테스트용 사용자 목록 조회 API
     * GET /api/users
     */
    @GetMapping("/users")
    public ResponseEntity<?> getTestUsers() {
        return ResponseEntity.ok(Map.of(
            "message", "테스트용 사용자 목록",
            "users", userDetailsService.getTestUsers(),
            "note", "모든 사용자의 비밀번호는 'password' 입니다."
        ));
    }

    /**
     * 관리자 전용 API 예제
     * GET /api/admin/info
     */
    @GetMapping("/admin/info")
    public ResponseEntity<?> getAdminInfo() {
        return ResponseEntity.ok(Map.of(
            "message", "관리자 전용 정보",
            "info", "이 API는 ADMIN 권한이 필요합니다.",
            "timestamp", System.currentTimeMillis()
        ));
    }

    /**
     * 애플리케이션 정보 API
     * GET /api/info
     */    
    @GetMapping("/info")
    public ResponseEntity<?> getAppInfo() {
        return ResponseEntity.ok(Map.of(
            "application", "Spring Security 예제",
            "version", "1.0.0",
            "description", "내용.md 6.1절, 6.2절 - 스프링 시큐리티 기본 구조와 API 기반 로그인 처리 예제",
            "endpoints", Map.of(
                "POST /api/login", "로그인 (username, password 필요)",
                "GET /api/users", "테스트 사용자 목록",
                "GET /api/me", "현재 인증된 사용자 정보",
                "GET /api/admin/info", "관리자 전용 정보 (ADMIN 권한 필요)",
                "GET /api/info", "애플리케이션 정보"
            )
        ));
    }
} 