package com.example.securityexam03.filter;

import com.example.securityexam03.service.JwtBlacklistService;
import com.example.securityexam03.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT 토큰을 통한 인증 처리 필터
 * 내용.md 6.5절 기반으로 구현
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final JwtBlacklistService jwtBlacklistService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService, 
                                   JwtBlacklistService jwtBlacklistService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.jwtBlacklistService = jwtBlacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        
        logger.debug("🔍 JWT 필터 처리 - URI: {}, Method: {}", requestURI, method);

        // 로그인 엔드포인트는 JWT 검증 제외
        if (requestURI.equals("/api/auth/login") || requestURI.equals("/api/auth/refresh")) {
            logger.debug("⏭️ 로그인/리프레시 엔드포인트 - JWT 검증 건너뛰기");
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            
            logger.debug("🎫 JWT 토큰 발견: {}", token.substring(0, Math.min(token.length(), 20)) + "...");

            try {
                // 블랙리스트 확인
                if (jwtBlacklistService.isBlacklisted(token)) {
                    logger.warn("🚫 블랙리스트에 포함된 토큰: {}", token.substring(0, Math.min(token.length(), 20)) + "...");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"Token is blacklisted\", \"message\": \"이 토큰은 무효화되었습니다\"}");
                    return;
                }

                // JWT 토큰 검증
                if (jwtUtil.validateToken(token)) {
                    String username = jwtUtil.getUsername(token);
                    List<String> roles = jwtUtil.getRoles(token);

                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        // UserDetails 로드
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                        // 권한 설정
                        List<SimpleGrantedAuthority> authorities = roles.stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList());

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        authorities);

                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        logger.info("✅ JWT 인증 성공 - 사용자: {}, 권한: {}", username, roles);
                    }
                } else {
                    logger.warn("❌ 유효하지 않은 JWT 토큰");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"Invalid token\", \"message\": \"유효하지 않은 토큰입니다\"}");
                    return;
                }
            } catch (Exception e) {
                logger.error("❌ JWT 토큰 처리 중 오류 발생: {}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Token processing error\", \"message\": \"토큰 처리 중 오류가 발생했습니다\"}");
                return;
            }
        } else {
            logger.debug("🔍 Authorization 헤더가 없거나 Bearer 토큰이 아닙니다");
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        
        // 공개 엔드포인트들은 필터링하지 않음
        return path.equals("/api/auth/login") ||
               path.equals("/api/auth/refresh") ||
               path.equals("/api/info") ||
               path.startsWith("/actuator/") ||
               path.startsWith("/static/") ||
               path.startsWith("/css/") ||
               path.startsWith("/js/") ||
               path.startsWith("/images/");
    }
} 