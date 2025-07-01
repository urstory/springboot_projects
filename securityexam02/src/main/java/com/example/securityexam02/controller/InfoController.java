package com.example.securityexam02.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.securityexam02.service.CustomUserDetailsService;

/**
 * 정보 조회 컨트롤러
 * 내용.md 6.3절 - UsernamePasswordAuthenticationFilter를 활용한 로그인 API 개발
 */
@RestController
@RequestMapping("/api")
public class InfoController {

    private final CustomUserDetailsService userDetailsService;

    public InfoController(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * 애플리케이션 정보 API
     * GET /api/info
     */    
    @GetMapping("/info")
    public ResponseEntity<?> getAppInfo() {
        return ResponseEntity.ok(Map.of(
            "application", "Spring Security 예제 - Custom Filter",
            "version", "2.0.0",
            "description", "내용.md 6.3절 - UsernamePasswordAuthenticationFilter를 활용한 로그인 API 개발",
            "features", Map.of(
                "customFilter", "UsernamePasswordAuthenticationFilter 상속",
                "jsonLogin", "JSON 형식 로그인 요청 처리",
                "authHandlers", "커스텀 성공/실패 핸들러"
            ),
            "endpoints", Map.of(
                "POST /api/login", "JSON 기반 로그인 (CustomAuthenticationFilter 처리)",
                "GET /api/users", "테스트 사용자 목록",
                "GET /api/info", "애플리케이션 정보"
            ),
            "differences_from_securityexam01", Map.of(
                "approach", "Controller 방식 → Filter 방식",
                "processing", "Spring MVC → Spring Security Filter Chain",
                "handler", "없음 → 성공/실패 핸들러"
            )
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
            "password", "모든 사용자의 비밀번호는 'password' 입니다.",
            "note", "로그인은 POST /api/login 으로 JSON 형식으로 요청하세요.",
            "example", Map.of(
                "url", "POST /api/login",
                "headers", Map.of("Content-Type", "application/json"),
                "body", Map.of("username", "filter_user", "password", "password")
            )
        ));
    }
} 