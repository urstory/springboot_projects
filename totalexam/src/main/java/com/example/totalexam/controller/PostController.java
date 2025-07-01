package com.example.totalexam.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.totalexam.dto.PostRequest;
import com.example.totalexam.dto.PostResponse;
import com.example.totalexam.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 게시글 관련 REST API 컨트롤러
 */
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PostController {
    
    private final PostService postService;
    
    /**
     * 게시글 생성 API (JWT 인증 필요)
     * POST /api/posts
     */
    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest postRequest,
                                                  Authentication authentication) {
        log.info("게시글 생성 API 호출: {} by {}", postRequest.getTitle(), authentication.getName());
        
        PostResponse response = postService.createPost(postRequest, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * 게시글 목록 조회 API (인증 불필요)
     * GET /api/posts
     */
    @GetMapping
    public ResponseEntity<Page<PostResponse>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("게시글 목록 조회 API 호출: page={}, size={}", page, size);
        
        Page<PostResponse> posts = postService.getAllPosts(page, size);
        return ResponseEntity.ok(posts);
    }
    
    /**
     * 게시글 상세 조회 API (인증 불필요)
     * GET /api/posts/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        log.info("게시글 상세 조회 API 호출: {}", id);
        
        PostResponse post = postService.getPost(id);
        return ResponseEntity.ok(post);
    }
    
    /**
     * 게시글 수정 API (JWT 인증 필요)
     * PUT /api/posts/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long id,
                                                  @Valid @RequestBody PostRequest postRequest,
                                                  Authentication authentication) {
        log.info("게시글 수정 API 호출: {} by {}", id, authentication.getName());
        
        PostResponse response = postService.updatePost(id, postRequest, authentication);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 게시글 삭제 API (JWT 인증 필요)
     * DELETE /api/posts/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id,
                                          Authentication authentication) {
        log.info("게시글 삭제 API 호출: {} by {}", id, authentication.getName());
        
        postService.deletePost(id, authentication);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * 게시글 검색 API (인증 불필요)
     * GET /api/posts/search?keyword={keyword}
     */
    @GetMapping("/search")
    public ResponseEntity<List<PostResponse>> searchPosts(@RequestParam String keyword) {
        log.info("게시글 검색 API 호출: {}", keyword);
        
        List<PostResponse> posts = postService.searchPosts(keyword);
        return ResponseEntity.ok(posts);
    }
    
    /**
     * 사용자별 게시글 조회 API (인증 불필요)
     * GET /api/posts/user/{username}
     */
    @GetMapping("/user/{username}")
    public ResponseEntity<List<PostResponse>> getPostsByUser(@PathVariable String username) {
        log.info("사용자별 게시글 조회 API 호출: {}", username);
        
        List<PostResponse> posts = postService.getPostsByUser(username);
        return ResponseEntity.ok(posts);
    }
} 