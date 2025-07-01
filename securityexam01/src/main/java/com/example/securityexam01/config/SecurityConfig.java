package com.example.securityexam01.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.securityexam01.service.CustomUserDetailsService;

/**
 * 스프링 시큐리티 설정 클래스
 * 내용.md 6.1절, 6.2절 - 스프링 시큐리티 기본 구조와 API 기반 로그인 처리 예제
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * BCrypt 방식으로 비밀번호를 암호화하는 PasswordEncoder Bean 등록
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 스프링 시큐리티 필터 체인 설정
     * 내용.md 6.2절 - API 기반 로그인 처리를 위한 설정
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 보호 기능 비활성화 (API 서버이므로)
            .csrf(csrf -> csrf.disable())
            
            // 요청 권한 설정
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/login", "/api/users", "/api/info").permitAll() // 로그인 API는 인증 없이 접근 허용
                .requestMatchers("/api/admin/**").hasRole("ADMIN") // 관리자 전용 API
                .anyRequest().authenticated() // 나머지 요청은 인증 필요
            )
            
            // 기본 Form 로그인 비활성화 (JSON 기반 API 사용)
            .formLogin(formLogin -> formLogin.disable())
            
            // HTTP Basic 인증 비활성화
            .httpBasic(httpBasic -> httpBasic.disable())
            
            // UserDetailsService 설정
            .userDetailsService(userDetailsService);

        return http.build();
    }

    /**
     * AuthenticationManager Bean 등록
     * 내용.md 6.2절 - API 기반 로그인 처리에 사용
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
} 