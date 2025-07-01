package com.example.datajpa.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.datajpa.entity.User;
import com.example.datajpa.repository.UserRepository;

/**
 * User 서비스 클래스
 * 내용.md 7장 - JPA와 Spring Data JDBC 예제
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 사용자 생성
     */
    public User createUser(String username, String email) {
        // 중복 검사
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("이미 존재하는 사용자명입니다: " + username);
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("이미 존재하는 이메일입니다: " + email);
        }

        User user = new User(username, email);
        return userRepository.save(user);
    }

    /**
     * 사용자 생성 (전체 정보)
     */
    public User createUser(String username, String email, String fullName, Integer age) {
        // 중복 검사
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("이미 존재하는 사용자명입니다: " + username);
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("이미 존재하는 이메일입니다: " + email);
        }

        User user = new User(username, email, fullName, age);
        return userRepository.save(user);
    }

    /**
     * 사용자 저장/업데이트
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * ID로 사용자 조회
     */
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * 사용자명으로 조회
     */
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 이메일로 조회
     */
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * 모든 사용자 조회
     */
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * 활성 사용자 조회
     */
    @Transactional(readOnly = true)
    public List<User> findActiveUsers() {
        return userRepository.findByActiveTrue();
    }

    /**
     * 나이 범위로 사용자 조회
     */
    @Transactional(readOnly = true)
    public List<User> findByAgeRange(Integer minAge, Integer maxAge) {
        return userRepository.findByAgeBetween(minAge, maxAge);
    }

    /**
     * 최근 가입 사용자 조회
     */
    @Transactional(readOnly = true)
    public List<User> findRecentUsers(LocalDateTime since) {
        return userRepository.findByCreatedAtAfter(since);
    }

    /**
     * 사용자명 검색
     */
    @Transactional(readOnly = true)
    public List<User> searchByUsername(String keyword) {
        return userRepository.findByUsernameContaining(keyword);
    }

    /**
     * 나이순 정렬 조회
     */
    @Transactional(readOnly = true)
    public List<User> findAllOrderByAge() {
        return userRepository.findAllByOrderByAgeAsc();
    }

    /**
     * 활성 사용자 중 특정 나이 이상 조회
     */
    @Transactional(readOnly = true)
    public List<User> findActiveUsersWithMinAge(Integer minAge) {
        return userRepository.findActiveUsersWithMinAge(minAge);
    }

    /**
     * 사용자 업데이트
     */
    public User updateUser(Long id, String fullName, Integer age) {
        Optional<User> optionalUser = findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setFullName(fullName);
            user.setAge(age);
            return save(user);
        }
        throw new RuntimeException("사용자를 찾을 수 없습니다: ID " + id);
    }

    /**
     * 사용자 비활성화
     */
    public User deactivateUser(Long id) {
        Optional<User> optionalUser = findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setActive(false);
            return save(user);
        }
        throw new RuntimeException("사용자를 찾을 수 없습니다: ID " + id);
    }

    /**
     * 사용자 삭제
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * 사용자 존재 여부 확인
     */
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    /**
     * 전체 사용자 개수
     */
    @Transactional(readOnly = true)
    public long count() {
        return userRepository.count();
    }
} 