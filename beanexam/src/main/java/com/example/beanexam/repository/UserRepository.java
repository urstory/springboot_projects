package com.example.beanexam.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * @Repository 어노테이션을 사용한 Bean 정의 예시
 * 데이터 접근 계층의 Bean
 */
@Slf4j
@Repository
public class UserRepository {
    
    private final Map<Long, UserData> userDatabase = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    @PostConstruct
    public void initialize() {
        log.info("🗄️ UserRepository Bean이 초기화되었습니다.");
        
        // 샘플 데이터 추가
        save(new UserData("Alice", "alice@example.com", "개발팀"));
        save(new UserData("Bob", "bob@example.com", "디자인팀"));
        save(new UserData("Charlie", "charlie@example.com", "마케팅팀"));
        
        log.info("🗄️ 샘플 사용자 {} 명이 추가되었습니다.", userDatabase.size());
    }
    
    public UserData save(UserData userData) {
        if (userData.getId() == null) {
            userData.setId(idGenerator.getAndIncrement());
        }
        userDatabase.put(userData.getId(), userData);
        log.debug("사용자 저장: {}", userData);
        return userData;
    }
    
    public UserData findById(Long id) {
        UserData user = userDatabase.get(id);
        log.debug("사용자 조회 - ID: {}, 결과: {}", id, user != null ? "발견" : "없음");
        return user;
    }
    
    public List<UserData> findAll() {
        List<UserData> users = userDatabase.values().stream().collect(Collectors.toList());
        log.debug("모든 사용자 조회: {} 명", users.size());
        return users;
    }
    
    public boolean deleteById(Long id) {
        UserData removed = userDatabase.remove(id);
        boolean deleted = removed != null;
        log.debug("사용자 삭제 - ID: {}, 결과: {}", id, deleted ? "성공" : "실패");
        return deleted;
    }
    
    public String getRepositoryInfo() {
        return String.format("UserRepository Bean - 저장된 사용자 수: %d, Bean 해시코드: %d",
                           userDatabase.size(), this.hashCode());
    }
    
    /**
     * 사용자 데이터를 표현하는 내부 클래스
     */
    public static class UserData {
        private Long id;
        private String name;
        private String email;
        private String department;
        
        public UserData() {}
        
        public UserData(String name, String email, String department) {
            this.name = name;
            this.email = email;
            this.department = department;
        }
        
        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
        
        @Override
        public String toString() {
            return String.format("UserData{id=%d, name='%s', email='%s', department='%s'}", 
                               id, name, email, department);
        }
    }
} 