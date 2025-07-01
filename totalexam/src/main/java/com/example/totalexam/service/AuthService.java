package com.example.totalexam.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.totalexam.dto.JwtResponse;
import com.example.totalexam.dto.LoginRequest;
import com.example.totalexam.entity.User;
import com.example.totalexam.repository.UserRepository;
import com.example.totalexam.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 인증 관련 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    
    /**
     * 사용자 로그인 처리
     */
    @Transactional(readOnly = true)
    public JwtResponse login(LoginRequest loginRequest) {
        try {
            log.debug("로그인 시도: {}", loginRequest.getUsername());
            
            // 사용자 인증
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );
            
            // JWT 토큰 생성
            String token = tokenProvider.generateToken(authentication);
            
            // 사용자 정보 조회
            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new BadCredentialsException("사용자를 찾을 수 없습니다"));
            
            log.info("로그인 성공: {}", loginRequest.getUsername());
            
            return new JwtResponse(token, user.getUsername(), user.getEmail(), user.getFullName());
            
        } catch (AuthenticationException e) {
            log.warn("로그인 실패: {} - {}", loginRequest.getUsername(), e.getMessage());
            throw new BadCredentialsException("잘못된 사용자명 또는 비밀번호입니다");
        }
    }
} 