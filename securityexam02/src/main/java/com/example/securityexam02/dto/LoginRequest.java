package com.example.securityexam02.dto;

/**
 * 로그인 요청 DTO
 * 내용.md 6.3절 - UsernamePasswordAuthenticationFilter를 활용한 로그인 API 개발
 */
public record LoginRequest(String username, String password) {
} 