package com.example.datajdbc.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.example.datajdbc.entity.User;
import com.example.datajdbc.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    // 모든 사용자 조회
    public List<User> getAllUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .toList();
    }
    
    // ID로 사용자 조회
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    // 사용자 생성 (중복 체크 포함)
    public User createUser(User user) {
        // 사용자명 중복 체크
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("이미 존재하는 사용자명입니다: " + user.getUsername());
        }
        
        // 이메일 중복 체크
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다: " + user.getEmail());
        }
        
        user.setCreatedAt(LocalDateTime.now());
        log.info("새 사용자 생성: {}", user.getUsername());
        return userRepository.save(user);
    }
    
    // 사용자 업데이트
    public User updateUser(Long id, User userDetails) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            
            // 사용자명 변경 시 중복 체크
            if (!user.getUsername().equals(userDetails.getUsername())) {
                if (userRepository.findByUsername(userDetails.getUsername()).isPresent()) {
                    throw new RuntimeException("이미 존재하는 사용자명입니다: " + userDetails.getUsername());
                }
                user.setUsername(userDetails.getUsername());
            }
            
            // 이메일 변경 시 중복 체크
            if (!user.getEmail().equals(userDetails.getEmail())) {
                if (userRepository.findByEmail(userDetails.getEmail()).isPresent()) {
                    throw new RuntimeException("이미 존재하는 이메일입니다: " + userDetails.getEmail());
                }
                user.setEmail(userDetails.getEmail());
            }
            
            user.setFullName(userDetails.getFullName());
            user.setAge(userDetails.getAge());
            user.setActive(userDetails.getActive());
            
            log.info("사용자 업데이트: ID {}, 사용자명 {}", id, user.getUsername());
            return userRepository.save(user);
        }
        throw new RuntimeException("사용자를 찾을 수 없습니다: ID " + id);
    }
    
    // 사용자 비활성화
    public User deactivateUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setActive(false);
            log.info("사용자 비활성화: ID {}, 사용자명 {}", id, user.getUsername());
            return userRepository.save(user);
        }
        throw new RuntimeException("사용자를 찾을 수 없습니다: ID " + id);
    }
    
    // 사용자 삭제
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            log.info("사용자 삭제: ID {}", id);
        } else {
            throw new RuntimeException("사용자를 찾을 수 없습니다: ID " + id);
        }
    }
    
    // 사용자명으로 찾기
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    // 이메일로 찾기
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    // 활성 사용자 조회
    public List<User> getActiveUsers() {
        return userRepository.findByActiveTrue();
    }
    
    // 비활성 사용자 조회
    public List<User> getInactiveUsers() {
        return userRepository.findByActiveFalse();
    }
    
    // 나이 범위로 사용자 찾기
    public List<User> getUsersByAgeRange(Integer minAge, Integer maxAge) {
        return userRepository.findByAgeBetween(minAge, maxAge);
    }
    
    // 특정 나이 이상 사용자 찾기
    public List<User> getUsersByMinAge(Integer age) {
        return userRepository.findByAgeGreaterThanEqual(age);
    }
    
    // 이름에 키워드 포함된 사용자 찾기
    public List<User> searchUsersByUsername(String keyword) {
        return userRepository.findByUsernameContainingIgnoreCase(keyword);
    }
    
    // 최근 가입 사용자 (특정 날짜 이후)
    public List<User> getRecentUsers(LocalDateTime since) {
        return userRepository.findByCreatedAtAfter(since);
    }
    
    // 활성 사용자 중 특정 나이 이상
    public List<User> getActiveUsersByMinAge(Integer age) {
        return userRepository.findActiveUsersByMinAge(age);
    }
    
    // 사용자 통계
    public long getTotalUserCount() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false).count();
    }
    
    public long getActiveUserCount() {
        return userRepository.findByActiveTrue().size();
    }
    
    public long getInactiveUserCount() {
        return userRepository.findByActiveFalse().size();
    }
} 