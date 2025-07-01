package com.example.datajdbc.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.datajdbc.entity.User;
import com.example.datajdbc.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    // 모든 사용자 조회
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    
    // ID로 사용자 조회
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    // 사용자 생성
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.ok(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // 사용자 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 사용자 비활성화
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<User> deactivateUser(@PathVariable Long id) {
        try {
            User deactivatedUser = userService.deactivateUser(id);
            return ResponseEntity.ok(deactivatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 사용자 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // 사용자명으로 찾기
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.getUserByUsername(username);
        return user.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    // 이메일로 찾기
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    // 활성 사용자 조회
    @GetMapping("/active")
    public List<User> getActiveUsers() {
        return userService.getActiveUsers();
    }
    
    // 비활성 사용자 조회
    @GetMapping("/inactive")
    public List<User> getInactiveUsers() {
        return userService.getInactiveUsers();
    }
    
    // 나이 범위로 사용자 찾기
    @GetMapping("/age-range")
    public List<User> getUsersByAgeRange(@RequestParam Integer minAge, @RequestParam Integer maxAge) {
        return userService.getUsersByAgeRange(minAge, maxAge);
    }
    
    // 특정 나이 이상 사용자 찾기
    @GetMapping("/min-age/{age}")
    public List<User> getUsersByMinAge(@PathVariable Integer age) {
        return userService.getUsersByMinAge(age);
    }
    
    // 사용자명 검색
    @GetMapping("/search")
    public List<User> searchUsersByUsername(@RequestParam String keyword) {
        return userService.searchUsersByUsername(keyword);
    }
    
    // 최근 가입 사용자 (최근 30일)
    @GetMapping("/recent")
    public List<User> getRecentUsers(@RequestParam(defaultValue = "30") int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return userService.getRecentUsers(since);
    }
    
    // 활성 사용자 중 특정 나이 이상
    @GetMapping("/active/min-age/{age}")
    public List<User> getActiveUsersByMinAge(@PathVariable Integer age) {
        return userService.getActiveUsersByMinAge(age);
    }
    
    // 사용자 통계
    @GetMapping("/statistics")
    public Map<String, Object> getUserStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userService.getTotalUserCount());
        stats.put("activeUsers", userService.getActiveUserCount());
        stats.put("inactiveUsers", userService.getInactiveUserCount());
        
        // 나이대별 분포
        List<User> allUsers = userService.getAllUsers();
        Map<String, Long> ageGroups = new HashMap<>();
        allUsers.forEach(user -> {
            String ageGroup = getAgeGroup(user.getAge());
            ageGroups.merge(ageGroup, 1L, Long::sum);
        });
        stats.put("ageDistribution", ageGroups);
        
        return stats;
    }
    
    private String getAgeGroup(Integer age) {
        if (age == null) return "Unknown";
        if (age < 20) return "Under 20";
        if (age < 30) return "20-29";
        if (age < 40) return "30-39";
        if (age < 50) return "40-49";
        if (age < 60) return "50-59";
        return "60+";
    }
} 