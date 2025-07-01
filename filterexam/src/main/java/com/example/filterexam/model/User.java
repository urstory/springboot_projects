package com.example.filterexam.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 사용자 모델 클래스
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private LocalDateTime lastLoginTime;
    private boolean active;
    
    public boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }
} 