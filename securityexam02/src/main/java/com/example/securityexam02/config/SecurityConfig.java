package com.example.securityexam02.config;

import java.io.IOException;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.securityexam02.filter.CustomAuthenticationFilter;
import com.example.securityexam02.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 스프링 시큐리티 설정 클래스 - CustomAuthenticationFilter 활용
 * 내용.md 6.3절 - UsernamePasswordAuthenticationFilter를 활용한 로그인 API 개발
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * DelegatingPasswordEncoder로 다양한 암호화 방식을 지원
     * {noop}, {bcrypt} 등의 프리픽스를 자동으로 처리
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * AuthenticationManager Bean 등록
     * 별도로 생성하여 순환 참조 문제 해결
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        // DaoAuthenticationProvider 생성
        org.springframework.security.authentication.dao.DaoAuthenticationProvider provider = 
            new org.springframework.security.authentication.dao.DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        
        // ProviderManager로 AuthenticationManager 생성
        return new org.springframework.security.authentication.ProviderManager(provider);
    }

    /**
     * 스프링 시큐리티 필터 체인 설정
     * CustomAuthenticationFilter를 등록하고 인증 핸들러를 설정
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        // CustomAuthenticationFilter 생성 및 설정
        CustomAuthenticationFilter customFilter = new CustomAuthenticationFilter(authenticationManager);

        // 인증 성공 시 처리할 핸들러 설정
        customFilter.setAuthenticationSuccessHandler(this::loginSuccessHandler);
        
        // 인증 실패 시 처리할 핸들러 설정
        customFilter.setAuthenticationFailureHandler(this::loginFailureHandler);

        http
            // CSRF 보호 기능 비활성화 (API 서버이므로)
            .csrf(csrf -> csrf.disable())
            
            // 요청 권한 설정 - 모든 요청 허용 (인증 필터에서 처리)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/info", "/api/users").permitAll() // 정보 조회 API는 인증 없이 허용
                .anyRequest().permitAll() // 나머지 요청도 일단 허용 (필터에서 처리)
            )
            
            // 기본 Form 로그인 비활성화 (커스텀 필터 사용)
            .formLogin(formLogin -> formLogin.disable())
            
            // HTTP Basic 인증 비활성화
            .httpBasic(httpBasic -> httpBasic.disable())
            
            // CustomAuthenticationFilter를 UsernamePasswordAuthenticationFilter 위치에 등록
            .addFilterAt(customFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 로그인 성공 시 처리 메서드
     * JSON 형태로 성공 응답을 반환
     */
    private void loginSuccessHandler(HttpServletRequest request, HttpServletResponse response, 
                                   Authentication authentication) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> responseBody = Map.of(
            "success", true,
            "message", "로그인 성공",
            "username", authentication.getName(),
            "authorities", authentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .toList(),
            "timestamp", System.currentTimeMillis()
        );

        String jsonResponse = objectMapper.writeValueAsString(responseBody);
        response.getWriter().write(jsonResponse);
    }

    /**
     * 로그인 실패 시 처리 메서드
     * JSON 형태로 실패 응답을 반환
     */
    private void loginFailureHandler(HttpServletRequest request, HttpServletResponse response, 
                                   AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> responseBody = Map.of(
            "success", false,
            "message", "로그인 실패: " + exception.getMessage(),
            "error", exception.getClass().getSimpleName(),
            "timestamp", System.currentTimeMillis()
        );

        String jsonResponse = objectMapper.writeValueAsString(responseBody);
        response.getWriter().write(jsonResponse);
    }
} 