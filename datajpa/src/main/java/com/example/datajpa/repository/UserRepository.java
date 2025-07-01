package com.example.datajpa.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.datajpa.entity.User;

/**
 * UserRepository 인터페이스
 * 내용.md 7장 - JPA와 Spring Data JDBC 예제
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 사용자명으로 조회
    Optional<User> findByUsername(String username);

    // 이메일로 조회
    Optional<User> findByEmail(String email);

    // 활성 사용자 조회
    List<User> findByActiveTrue();

    // 나이 범위로 조회
    List<User> findByAgeBetween(Integer minAge, Integer maxAge);

    // 특정 날짜 이후 생성된 사용자
    List<User> findByCreatedAtAfter(LocalDateTime date);

    // 사용자명에 특정 문자열 포함
    List<User> findByUsernameContaining(String keyword);

    // 나이순 정렬
    List<User> findAllByOrderByAgeAsc();

    // 커스텀 쿼리 - 활성 사용자 중 특정 나이 이상
    @Query("SELECT u FROM User u WHERE u.active = true AND u.age >= :minAge")
    List<User> findActiveUsersWithMinAge(Integer minAge);

    // 사용자명 존재 여부
    boolean existsByUsername(String username);

    // 이메일 존재 여부
    boolean existsByEmail(String email);
} 