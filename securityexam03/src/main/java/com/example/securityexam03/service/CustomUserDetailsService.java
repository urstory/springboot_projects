package com.example.securityexam03.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 사용자 인증 정보를 제공하는 서비스
 * JWT 예제를 위한 테스트 사용자 데이터 포함
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // 테스트용 사용자 데이터 (실제 운영에서는 데이터베이스 사용)
    private final Map<String, UserInfo> testUsers;

    public CustomUserDetailsService() {
        this.testUsers = Map.of(
            "user1", new UserInfo("user1", passwordEncoder.encode("password"), List.of("ROLE_USER")),
            "admin", new UserInfo("admin", passwordEncoder.encode("password"), List.of("ROLE_ADMIN", "ROLE_USER")),
            "guest", new UserInfo("guest", passwordEncoder.encode("password"), List.of("ROLE_GUEST")),
            "jwt_user", new UserInfo("jwt_user", passwordEncoder.encode("password"), List.of("ROLE_USER")),
            "jwt_admin", new UserInfo("jwt_admin", passwordEncoder.encode("admin123"), List.of("ROLE_ADMIN", "ROLE_USER"))
        );
        
        logger.info("✅ CustomUserDetailsService 초기화 완료 - 테스트 사용자 {} 명 로드", testUsers.size());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("🔍 사용자 정보 조회 요청: {}", username);
        
        UserInfo userInfo = testUsers.get(username);
        if (userInfo == null) {
            logger.warn("❌ 사용자를 찾을 수 없음: {}", username);
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }

        List<SimpleGrantedAuthority> authorities = userInfo.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        UserDetails userDetails = User.builder()
                .username(userInfo.username)
                .password(userInfo.password)
                .authorities(authorities)
                .build();

        logger.debug("✅ 사용자 정보 조회 성공: {}, 권한: {}", username, userInfo.roles);
        return userDetails;
    }

    /**
     * 사용자 존재 여부 확인
     * @param username 사용자명
     * @return 존재 여부
     */
    public boolean userExists(String username) {
        return testUsers.containsKey(username);
    }

    /**
     * 사용자 권한 조회
     * @param username 사용자명
     * @return 권한 목록
     */
    public List<String> getUserRoles(String username) {
        UserInfo userInfo = testUsers.get(username);
        return userInfo != null ? userInfo.roles : List.of();
    }

    /**
     * 모든 테스트 사용자 목록 조회
     * @return 사용자 목록
     */
    public List<TestUser> getAllTestUsers() {
        return testUsers.entrySet().stream()
                .map(entry -> new TestUser(
                    entry.getKey(), 
                    entry.getValue().roles,
                    entry.getKey().equals("jwt_admin") ? "admin123" : "password"
                ))
                .toList();
    }

    /**
     * 내부 사용자 정보 클래스
     */
    private static class UserInfo {
        final String username;
        final String password;
        final List<String> roles;

        UserInfo(String username, String password, List<String> roles) {
            this.username = username;
            this.password = password;
            this.roles = roles;
        }
    }

    /**
     * 테스트 사용자 정보 DTO
     */
    public static class TestUser {
        public final String username;
        public final List<String> roles;
        public final String plainPassword;

        public TestUser(String username, List<String> roles, String plainPassword) {
            this.username = username;
            this.roles = roles;
            this.plainPassword = plainPassword;
        }
    }
} 