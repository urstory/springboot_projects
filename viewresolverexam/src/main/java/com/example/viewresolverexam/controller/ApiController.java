package com.example.viewresolverexam.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.viewresolverexam.dto.User;

/**
 * MessageConverter를 사용하는 REST 컨트롤러
 * - 객체를 직접 반환하여 JSON으로 변환
 * - MappingJackson2HttpMessageConverter가 객체를 JSON으로 직렬화
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    // 샘플 사용자 데이터
    private final List<User> users = List.of(
            new User(1L, "Alice", 25, "alice@example.com"),
            new User(2L, "Bob", 30, "bob@example.com"),
            new User(3L, "Charlie", 35, "charlie@example.com")
    );

    /**
     * API 정보 반환
     * MessageConverter 사용: Map 객체 → JSON 변환
     */
    @GetMapping("/info")
    public Map<String, Object> getApiInfo() {
        return Map.of(
                "title", "MessageConverter API 예제",
                "description", "MessageConverter를 통해 JSON 응답이 반환됩니다.",
                "version", "1.0",
                "timestamp", System.currentTimeMillis()
        );
    }

    /**
     * 모든 사용자 조회
     * MessageConverter 사용: List<User> → JSON 배열 변환
     */
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return users; // 객체 직접 반환 → MessageConverter가 JSON으로 변환
    }

    /**
     * 특정 사용자 조회
     * MessageConverter 사용: User 객체 → JSON 변환
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
                
        if (user != null) {
            return ResponseEntity.ok(user); // 객체 반환 → MessageConverter가 JSON으로 변환
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 사용자 통계 정보
     * MessageConverter 사용: Map 객체 → JSON 변환
     */
    @GetMapping("/users/stats")
    public Map<String, Object> getUserStats() {
        double avgAge = users.stream()
                .mapToInt(User::getAge)
                .average()
                .orElse(0.0);
                
        return Map.of(
                "totalUsers", users.size(),
                "averageAge", Math.round(avgAge * 100.0) / 100.0,
                "domains", users.stream()
                        .map(user -> user.getEmail().split("@")[1])
                        .distinct()
                        .toList()
        );
    }

    /**
     * 사용자 생성 (예제용)
     * MessageConverter 사용: 요청 JSON → User 객체 변환, User 객체 → 응답 JSON 변환
     */
    @PostMapping("/users")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody User user) {
        // 실제로는 데이터베이스에 저장하겠지만, 예제에서는 단순히 응답만 반환
        return ResponseEntity.ok(Map.of(
                "message", "사용자가 생성되었습니다.",
                "user", user,
                "timestamp", System.currentTimeMillis()
        ));
    }
} 