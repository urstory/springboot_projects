package com.example.securityexam02.filter;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JSON 형식의 로그인 요청을 처리하는 커스텀 인증 필터
 * 내용.md 6.3절 - UsernamePasswordAuthenticationFilter를 활용한 로그인 API 개발
 */
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/api/login"); // 로그인 처리 URL 설정
    }

    /**
     * JSON 형식의 로그인 요청을 처리하는 메서드
     * 기본 구현체는 Form 데이터만 처리하므로 JSON 처리를 위해 오버라이드
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) 
            throws AuthenticationException {
        
        // POST 요청이 아니거나 Content-Type이 application/json이 아닌 경우 예외 처리
        if (!request.getMethod().equals("POST") || 
            !MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType())) {
            throw new AuthenticationServiceException("지원하지 않는 인증 방식입니다. POST 요청과 JSON 형식만 지원합니다.");
        }

        try {
            // JSON 요청 본문을 LoginRequest 객체로 파싱
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            
            // 사용자명과 비밀번호 검증
            if (loginRequest.username() == null || loginRequest.username().trim().isEmpty()) {
                throw new AuthenticationServiceException("사용자명이 필요합니다.");
            }
            if (loginRequest.password() == null || loginRequest.password().trim().isEmpty()) {
                throw new AuthenticationServiceException("비밀번호가 필요합니다.");
            }

            // UsernamePasswordAuthenticationToken 생성
            UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(
                    loginRequest.username().trim(), 
                    loginRequest.password()
                );

            // 요청 세부사항을 인증 토큰에 설정 (IP 주소, 세션 ID 등)
            setDetails(request, authRequest);
            
            // AuthenticationManager를 통해 실제 인증 수행
            return this.getAuthenticationManager().authenticate(authRequest);
            
        } catch (IOException e) {
            throw new AuthenticationServiceException("인증 요청 처리 실패: JSON 파싱 오류", e);
        }
    }

    /**
     * 로그인 요청 DTO
     * JSON 형식의 로그인 요청을 받기 위한 레코드 클래스
     */
    public record LoginRequest(String username, String password) {}
} 