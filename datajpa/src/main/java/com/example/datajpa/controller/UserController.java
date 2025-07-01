package com.example.datajpa.controller;

import java.time.LocalDateTime;
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

import com.example.datajpa.entity.User;
import com.example.datajpa.service.UserService;

/**
 * User REST API Controller
 * 내용.md 7장 - JPA와 Spring Data JDBC 예제
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 모든 사용자 조회
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    /**
     * ID로 사용자 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 사용자명으로 조회
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        return user.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 이메일로 조회
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.findByEmail(email);
        return user.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 사용자 생성
     */
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody Map<String, Object> request) {
        try {
            String username = (String) request.get("username");
            String email = (String) request.get("email");
            String fullName = (String) request.get("fullName");
            Integer age = request.get("age") != null ? (Integer) request.get("age") : null;

            User user;
            if (fullName != null && age != null) {
                user = userService.createUser(username, email, fullName, age);
            } else {
                user = userService.createUser(username, email);
            }
            
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 사용자 업데이트
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            String fullName = (String) request.get("fullName");
            Integer age = request.get("age") != null ? (Integer) request.get("age") : null;
            
            User updatedUser = userService.updateUser(id, fullName, age);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 사용자 비활성화
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id) {
        try {
            User user = userService.deactivateUser(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 사용자 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.existsById(id)) {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 활성 사용자 조회
     */
    @GetMapping("/active")
    public ResponseEntity<List<User>> getActiveUsers() {
        List<User> users = userService.findActiveUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * 나이 범위로 사용자 조회
     */
    @GetMapping("/age-range")
    public ResponseEntity<List<User>> getUsersByAgeRange(
            @RequestParam Integer minAge, 
            @RequestParam Integer maxAge) {
        List<User> users = userService.findByAgeRange(minAge, maxAge);
        return ResponseEntity.ok(users);
    }

    /**
     * 최근 가입 사용자 조회
     */
    @GetMapping("/recent")
    public ResponseEntity<List<User>> getRecentUsers(@RequestParam(defaultValue = "7") int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        List<User> users = userService.findRecentUsers(since);
        return ResponseEntity.ok(users);
    }

    /**
     * 사용자명 검색
     */
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String keyword) {
        List<User> users = userService.searchByUsername(keyword);
        return ResponseEntity.ok(users);
    }

    /**
     * 나이순 정렬 조회
     */
    @GetMapping("/ordered-by-age")
    public ResponseEntity<List<User>> getUsersOrderedByAge() {
        List<User> users = userService.findAllOrderByAge();
        return ResponseEntity.ok(users);
    }

    /**
     * 활성 사용자 중 특정 나이 이상 조회
     */
    @GetMapping("/active/min-age/{minAge}")
    public ResponseEntity<List<User>> getActiveUsersWithMinAge(@PathVariable Integer minAge) {
        List<User> users = userService.findActiveUsersWithMinAge(minAge);
        return ResponseEntity.ok(users);
    }

    /**
     * 사용자 통계 정보
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getUserStats() {
        long totalCount = userService.count();
        List<User> activeUsers = userService.findActiveUsers();
        long activeCount = activeUsers.size();
        
        // 최근 7일 가입자 수
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        List<User> recentUsers = userService.findRecentUsers(weekAgo);
        long recentCount = recentUsers.size();
        
        Map<String, Object> stats = Map.of(
            "totalCount", totalCount,
            "activeCount", activeCount,
            "recentWeekCount", recentCount,
            "activeRatio", totalCount > 0 ? (double) activeCount / totalCount : 0.0
        );
        
        return ResponseEntity.ok(stats);
    }
} 