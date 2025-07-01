package com.example.totalexam.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.totalexam.entity.Post;
import com.example.totalexam.entity.User;

/**
 * 게시글 리포지토리 인터페이스
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    /**
     * 작성자별 게시글 조회
     */
    List<Post> findByAuthor(User author);
    
    /**
     * 제목으로 게시글 검색
     */
    List<Post> findByTitleContainingIgnoreCase(String title);
    
    /**
     * 내용으로 게시글 검색
     */
    List<Post> findByContentContainingIgnoreCase(String content);
    
    /**
     * 제목 또는 내용으로 게시글 검색
     */
    @Query("SELECT p FROM Post p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Post> findByTitleOrContentContaining(@Param("keyword") String keyword);
    
    /**
     * 최신 게시글 조회 (페이징)
     */
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    /**
     * 특정 기간 내 게시글 조회
     */
    List<Post> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * 작성자별 게시글 개수 조회
     */
    long countByAuthor(User author);
} 