package com.example.securityexam01.service;

import java.util.Map;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 사용자 인증을 위한 UserDetailsService 구현
 * 내용.md 6.2절 - API 기반 로그인 처리 구현 실습 예제
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    // 간단한 테스트용 사용자 데이터 (실제로는 DB에서 가져오는 것을 권장)
    // 비밀번호는 BCrypt로 암호화된 값 (원본: "password")
    private static final Map<String, UserData> USERS = Map.of(
        "user1", new UserData("user1", "$2a$10$UQnwZID9aQ.wtQLPDO8f1.OsRCbzOk4dBrVV.X2W2Pnew4KEzFBm.", "USER"),
        "admin", new UserData("admin", "$2a$10$UQnwZID9aQ.wtQLPDO8f1.OsRCbzOk4dBrVV.X2W2Pnew4KEzFBm.", "ADMIN"),
        "guest", new UserData("guest", "$2a$10$UQnwZID9aQ.wtQLPDO8f1.OsRCbzOk4dBrVV.X2W2Pnew4KEzFBm.", "GUEST")
    );

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserData userData = USERS.get(username);
        if (userData == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }

        return User.builder()
                .username(userData.username())
                .password(userData.password())
                .roles(userData.role())
                .build();
    }

    /**
     * 사용자 데이터를 담는 레코드 클래스
     */
    private record UserData(String username, String password, String role) {}

    /**
     * 테스트용 사용자 목록 조회
     */
    public Map<String, String> getTestUsers() {
        return Map.of(
            "user1", "USER 권한",
            "admin", "ADMIN 권한", 
            "guest", "GUEST 권한"
        );
    }
} 