package com.example.securityexam01.dto;

/**
 * 로그인 요청 DTO
 * 내용.md 6.2절 - API 기반 로그인 처리 구현 실습 예제
 */
public record LoginRequest(String username, String password) {
} 