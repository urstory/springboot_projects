package com.example.securityexam03.service;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * JWT 토큰 블랙리스트 관리 서비스
 * 내용.md 6.6절 기반으로 구현
 */
@Service
public class JwtBlacklistService {

    private static final Logger logger = LoggerFactory.getLogger(JwtBlacklistService.class);
    private static final String BLACKLIST_PREFIX = "jwt:blacklist:";

    @Autowired(required = false)
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 토큰을 블랙리스트에 추가
     * @param token JWT 토큰
     * @param expirationMillis 만료 시간 (밀리초)
     */
    public void blacklistToken(String token, long expirationMillis) {
        if (redisTemplate != null) {
            try {
                String key = BLACKLIST_PREFIX + token;
                redisTemplate.opsForValue().set(key, "blacklisted", expirationMillis, TimeUnit.MILLISECONDS);
                logger.info("✅ JWT 토큰 블랙리스트 추가: {}", token.substring(0, Math.min(token.length(), 20)) + "...");
            } catch (Exception e) {
                logger.error("❌ JWT 토큰 블랙리스트 추가 실패: {}", e.getMessage());
            }
        } else {
            logger.warn("⚠️ Redis가 설정되지 않아 블랙리스트 기능을 사용할 수 없습니다");
        }
    }

    /**
     * 토큰이 블랙리스트에 있는지 확인
     * @param token JWT 토큰
     * @return 블랙리스트 포함 여부
     */
    public boolean isBlacklisted(String token) {
        if (redisTemplate != null) {
            try {
                String key = BLACKLIST_PREFIX + token;
                Boolean exists = redisTemplate.hasKey(key);
                boolean isBlacklisted = exists != null && exists;
                
                if (isBlacklisted) {
                    logger.debug("🚫 블랙리스트에 포함된 토큰 발견: {}", token.substring(0, Math.min(token.length(), 20)) + "...");
                }
                
                return isBlacklisted;
            } catch (Exception e) {
                logger.error("❌ JWT 토큰 블랙리스트 확인 실패: {}", e.getMessage());
                return false;
            }
        } else {
            // Redis가 없으면 블랙리스트 기능 비활성화
            return false;
        }
    }

    /**
     * 토큰을 블랙리스트에서 제거
     * @param token JWT 토큰
     */
    public void removeFromBlacklist(String token) {
        if (redisTemplate != null) {
            try {
                String key = BLACKLIST_PREFIX + token;
                redisTemplate.delete(key);
                logger.info("✅ JWT 토큰 블랙리스트에서 제거: {}", token.substring(0, Math.min(token.length(), 20)) + "...");
            } catch (Exception e) {
                logger.error("❌ JWT 토큰 블랙리스트 제거 실패: {}", e.getMessage());
            }
        } else {
            logger.warn("⚠️ Redis가 설정되지 않아 블랙리스트 기능을 사용할 수 없습니다");
        }
    }

    /**
     * 블랙리스트의 모든 토큰 제거 (관리자용)
     */
    public void clearBlacklist() {
        if (redisTemplate != null) {
            try {
                String pattern = BLACKLIST_PREFIX + "*";
                var keys = redisTemplate.keys(pattern);
                if (keys != null && !keys.isEmpty()) {
                    redisTemplate.delete(keys);
                    logger.info("✅ JWT 블랙리스트 전체 삭제 완료: {} 개 토큰", keys.size());
                } else {
                    logger.info("ℹ️ 삭제할 블랙리스트 토큰이 없습니다");
                }
            } catch (Exception e) {
                logger.error("❌ JWT 블랙리스트 전체 삭제 실패: {}", e.getMessage());
            }
        } else {
            logger.warn("⚠️ Redis가 설정되지 않아 블랙리스트 기능을 사용할 수 없습니다");
        }
    }

    /**
     * 블랙리스트 토큰 개수 조회
     * @return 블랙리스트 토큰 개수
     */
    public long getBlacklistCount() {
        if (redisTemplate != null) {
            try {
                String pattern = BLACKLIST_PREFIX + "*";
                var keys = redisTemplate.keys(pattern);
                return keys != null ? keys.size() : 0;
            } catch (Exception e) {
                logger.error("❌ JWT 블랙리스트 개수 조회 실패: {}", e.getMessage());
                return 0;
            }
        } else {
            return 0;
        }
    }
} 