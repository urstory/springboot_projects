package com.example.securityexam02;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "password";
        String encodedPassword = encoder.encode(rawPassword);
        
        System.out.println("원본 비밀번호: " + rawPassword);
        System.out.println("암호화된 비밀번호: " + encodedPassword);
        
        // 검증 테스트
        boolean matches = encoder.matches(rawPassword, encodedPassword);
        System.out.println("비밀번호 일치 여부: " + matches);
        
        // 기존 해시값과의 비교 테스트
        String existingHash = "$2a$10$XZXuEo2Jho5pRtRm3n8kiOgC.Y8/Oq9g7y8.JLvM.vCv0dcNn6aXy";
        boolean existingMatches = encoder.matches(rawPassword, existingHash);
        System.out.println("기존 해시값과의 일치 여부: " + existingMatches);
    }
} 