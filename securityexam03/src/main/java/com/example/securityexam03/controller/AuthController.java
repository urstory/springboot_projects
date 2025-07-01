package com.example.securityexam03.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.securityexam03.dto.LoginRequest;
import com.example.securityexam03.dto.LoginResponse;
import com.example.securityexam03.service.CustomUserDetailsService;
import com.example.securityexam03.service.JwtBlacklistService;
import com.example.securityexam03.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

/**
 * JWT 인증 컨트롤러
 * 내용.md 6.5절 기반으로 구현
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtBlacklistService jwtBlacklistService;

    /**
     * JWT 기반 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        logger.info("🔐 로그인 시도 - 사용자: {}", loginRequest.getUsername());

        try {
            // 사용자 인증
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // 권한 정보 추출
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            // JWT 토큰 생성
            String token = jwtUtil.createToken(userDetails.getUsername(), roles);
            Date expiresAt = new Date(System.currentTimeMillis() + jwtUtil.getTokenValidityInMilliseconds());

            LoginResponse response = new LoginResponse(token, userDetails.getUsername(), roles, expiresAt);
            
            logger.info("✅ 로그인 성공 - 사용자: {}, 권한: {}", userDetails.getUsername(), roles);
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            logger.warn("❌ 로그인 실패 - 잘못된 인증 정보: {}", loginRequest.getUsername());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid credentials");
            errorResponse.put("message", "사용자명 또는 비밀번호가 올바르지 않습니다");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (Exception e) {
            logger.error("❌ 로그인 처리 중 오류 발생: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Authentication failed");
            errorResponse.put("message", "인증 처리 중 오류가 발생했습니다");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * JWT 기반 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            
            try {
                // 토큰 만료 시간 조회
                Date expirationDate = jwtUtil.getExpirationDate(token);
                if (expirationDate != null) {
                    long remainingTime = expirationDate.getTime() - System.currentTimeMillis();
                    if (remainingTime > 0) {
                        // 블랙리스트에 추가
                        jwtBlacklistService.blacklistToken(token, remainingTime);
                    }
                }

                // 현재 인증 정보 제거
                SecurityContextHolder.clearContext();

                logger.info("✅ 로그아웃 성공 - 토큰이 블랙리스트에 추가되었습니다");
                
                Map<String, Object> response = new HashMap<>();
                response.put("message", "로그아웃 성공");
                response.put("timestamp", new Date());
                return ResponseEntity.ok(response);

            } catch (Exception e) {
                logger.error("❌ 로그아웃 처리 중 오류 발생: {}", e.getMessage());
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Logout failed");
                errorResponse.put("message", "로그아웃 처리 중 오류가 발생했습니다");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "No token provided");
            errorResponse.put("message", "토큰이 제공되지 않았습니다");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * 현재 사용자 정보 조회
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication != null && authentication.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                
                List<String> roles = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList();

                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("username", userDetails.getUsername());
                userInfo.put("roles", roles);
                userInfo.put("authenticated", true);
                userInfo.put("timestamp", new Date());

                return ResponseEntity.ok(userInfo);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Not authenticated");
                errorResponse.put("message", "인증되지 않은 사용자입니다");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
        } catch (Exception e) {
            logger.error("❌ 사용자 정보 조회 중 오류 발생: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "User info retrieval failed");
            errorResponse.put("message", "사용자 정보 조회 중 오류가 발생했습니다");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * 토큰 유효성 검증
     */
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            
            try {
                boolean isValid = jwtUtil.validateToken(token) && !jwtBlacklistService.isBlacklisted(token);
                
                Map<String, Object> response = new HashMap<>();
                response.put("valid", isValid);
                response.put("timestamp", new Date());
                
                if (isValid) {
                    String username = jwtUtil.getUsername(token);
                    List<String> roles = jwtUtil.getRoles(token);
                    response.put("username", username);
                    response.put("roles", roles);
                    response.put("expiresAt", jwtUtil.getExpirationDate(token));
                }
                
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                logger.error("❌ 토큰 검증 중 오류 발생: {}", e.getMessage());
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("valid", false);
                errorResponse.put("error", "Token validation failed");
                errorResponse.put("message", "토큰 검증 중 오류가 발생했습니다");
                return ResponseEntity.ok(errorResponse);
            }
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("valid", false);
            response.put("error", "No token provided");
            response.put("message", "토큰이 제공되지 않았습니다");
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 테스트 사용자 목록 조회
     */
    @GetMapping("/users")
    public ResponseEntity<?> getTestUsers() {
        try {
            List<CustomUserDetailsService.TestUser> users = userDetailsService.getAllTestUsers();
            
            Map<String, Object> response = new HashMap<>();
            response.put("users", users);
            response.put("count", users.size());
            response.put("message", "테스트 사용자 목록");
            response.put("timestamp", new Date());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("❌ 테스트 사용자 목록 조회 중 오류 발생: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "User list retrieval failed");
            errorResponse.put("message", "사용자 목록 조회 중 오류가 발생했습니다");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
} 