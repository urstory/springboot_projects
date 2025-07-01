package com.example.myspringapp.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.myspringapp.dto.User;

@Repository
public class UserRepository {
    private final Map<Long, User> userStore = new HashMap<>();
    private Long sequence = 0L;
    
    // 사용자 저장 (Create)
    public User save(User user) {
        user.setId(++sequence);
        userStore.put(user.getId(), user);
        return user;
    }
    
    // 모든 사용자 조회 (Read All)
    public List<User> findAll() {
        return new ArrayList<>(userStore.values());
    }
    
    // 특정 사용자 조회 (Read One)
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userStore.get(id));
    }
    
    // 사용자 정보 수정 (Update)
    public Optional<User> update(Long id, User user) {
        User existingUser = userStore.get(id);
        if (existingUser == null) {
            return Optional.empty();
        }
        existingUser.setName(user.getName());
        existingUser.setAge(user.getAge());
        return Optional.of(existingUser);
    }
    
    // 사용자 삭제 (Delete)
    public boolean delete(Long id) {
        return userStore.remove(id) != null;
    }
} 