package com.example.securityexam03.util;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

/**
 * JWT 토큰 생성 및 검증을 위한 유틸리티 클래스
 * 내용.md 6.5절 기반으로 구현
 */
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    
    @Value("${jwt.secret:your-secret-key-for-jwt-token-generation}")
    private String jwtSecret;
    
    @Value("${jwt.expiration:3600000}")
    private int jwtExpiration; // 1시간 (밀리초)
    
    private final String secretKey;
    
    public JwtUtil() {
        // 기본 시크릿 키 설정
        this.secretKey = Base64.getEncoder().encodeToString("your-secret-key-for-jwt-token-generation".getBytes());
    }

    /**
     * JWT 토큰 생성
     * @param username 사용자명
     * @param roles 사용자 권한 목록
     * @return JWT 토큰
     */
    public String createToken(String username, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);
        claims.put("tokenType", "ACCESS_TOKEN");

        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtExpiration);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .setIssuer("securityexam03")
                .setAudience("api.securityexam03.com")
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();

        logger.info("✅ JWT 토큰 생성 완료 - 사용자: {}, 만료시간: {}", username, validity);
        return token;
    }

    /**
     * JWT 토큰 검증
     * @param token JWT 토큰
     * @return 토큰 유효성 여부
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token);
            
            logger.debug("✅ JWT 토큰 검증 성공");
            return true;
        } catch (ExpiredJwtException e) {
            logger.warn("❌ JWT 토큰 만료: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.warn("❌ 지원되지 않는 JWT 토큰: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.warn("❌ 잘못된 형식의 JWT 토큰: {}", e.getMessage());
        } catch (SecurityException e) {
            logger.warn("❌ JWT 토큰 서명 오류: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warn("❌ JWT 토큰이 비어있음: {}", e.getMessage());
        }
        return false;
    }

    /**
     * JWT 토큰에서 사용자 이름 추출
     * @param token JWT 토큰
     * @return 사용자 이름
     */
    public String getUsername(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            return claims.getSubject();
        } catch (Exception e) {
            logger.error("❌ JWT 토큰에서 사용자명 추출 실패: {}", e.getMessage());
            return null;
        }
    }

    /**
     * JWT 토큰에서 사용자 권한 추출
     * @param token JWT 토큰
     * @return 사용자 권한 목록
     */
    @SuppressWarnings("unchecked")
    public List<String> getRoles(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            return (List<String>) claims.get("roles");
        } catch (Exception e) {
            logger.error("❌ JWT 토큰에서 권한 추출 실패: {}", e.getMessage());
            return List.of();
        }
    }

    /**
     * JWT 토큰의 만료 시간 조회
     * @param token JWT 토큰
     * @return 만료 시간
     */
    public Date getExpirationDate(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            return claims.getExpiration();
        } catch (Exception e) {
            logger.error("❌ JWT 토큰에서 만료시간 추출 실패: {}", e.getMessage());
            return null;
        }
    }

    /**
     * JWT 토큰 만료 여부 확인
     * @param token JWT 토큰
     * @return 만료 여부
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = getExpirationDate(token);
            return expiration != null && expiration.before(new Date());
        } catch (Exception e) {
            logger.error("❌ JWT 토큰 만료 여부 확인 실패: {}", e.getMessage());
            return true;
        }
    }

    /**
     * 토큰 유효시간 조회 (밀리초)
     * @return 토큰 유효시간
     */
    public long getTokenValidityInMilliseconds() {
        return jwtExpiration;
    }
} 