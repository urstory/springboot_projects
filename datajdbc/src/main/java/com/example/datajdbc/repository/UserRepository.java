package com.example.datajdbc.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.datajdbc.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    
    // 사용자명으로 찾기
    Optional<User> findByUsername(String username);
    
    // 이메일로 찾기
    Optional<User> findByEmail(String email);
    
    // 활성 사용자 찾기
    List<User> findByActiveTrue();
    
    // 비활성 사용자 찾기
    List<User> findByActiveFalse();
    
    // 나이 범위로 사용자 찾기
    List<User> findByAgeBetween(Integer minAge, Integer maxAge);
    
    // 특정 나이 이상 사용자 찾기
    List<User> findByAgeGreaterThanEqual(Integer age);
    
    // 이름에 키워드 포함된 사용자 찾기
    List<User> findByUsernameContainingIgnoreCase(String keyword);
    
    // 특정 날짜 이후 가입한 사용자
    List<User> findByCreatedAtAfter(LocalDateTime date);
    
    // 활성 사용자 중 특정 나이 이상
    @Query("SELECT * FROM users WHERE active = true AND age >= :age")
    List<User> findActiveUsersByMinAge(@Param("age") Integer age);
    
    // 사용자 이름과 이메일로 찾기
    Optional<User> findByUsernameAndEmail(String username, String email);
} 