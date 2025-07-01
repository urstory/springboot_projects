package com.example.totalexam.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.totalexam.dto.PostRequest;
import com.example.totalexam.dto.PostResponse;
import com.example.totalexam.entity.Post;
import com.example.totalexam.entity.User;
import com.example.totalexam.repository.PostRepository;
import com.example.totalexam.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 게시글 관련 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    
    /**
     * 게시글 생성
     */
    @Transactional
    public PostResponse createPost(PostRequest postRequest, Authentication authentication) {
        String username = authentication.getName();
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + username));
        
        Post post = new Post(postRequest.getTitle(), postRequest.getContent(), author);
        Post savedPost = postRepository.save(post);
        
        log.info("게시글 생성 완료: {} by {}", savedPost.getId(), username);
        return new PostResponse(savedPost);
    }
    
    /**
     * 모든 게시글 조회 (페이징)
     */
    @Transactional(readOnly = true)
    public Page<PostResponse> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> postPage = postRepository.findAllByOrderByCreatedAtDesc(pageable);
        
        return postPage.map(PostResponse::new);
    }
    
    /**
     * 게시글 상세 조회
     */
    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + id));
        
        return new PostResponse(post);
    }
    
    /**
     * 게시글 수정
     */
    @Transactional
    public PostResponse updatePost(Long id, PostRequest postRequest, Authentication authentication) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + id));
        
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        
        // 작성자이거나 관리자여야 수정 가능
        if (!post.getAuthor().getUsername().equals(username) && !isAdmin) {
            throw new AccessDeniedException("게시글 수정 권한이 없습니다");
        }
        
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.preUpdate(); // 수정 시간 업데이트
        
        Post updatedPost = postRepository.save(post);
        log.info("게시글 수정 완료: {} by {}", id, username);
        
        return new PostResponse(updatedPost);
    }
    
    /**
     * 게시글 삭제
     */
    @Transactional
    public void deletePost(Long id, Authentication authentication) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + id));
        
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        
        // 작성자이거나 관리자여야 삭제 가능
        if (!post.getAuthor().getUsername().equals(username) && !isAdmin) {
            throw new AccessDeniedException("게시글 삭제 권한이 없습니다");
        }
        
        postRepository.deleteById(id);
        log.info("게시글 삭제 완료: {} by {} (admin: {})", id, username, isAdmin);
    }
    
    /**
     * 게시글 검색
     */
    @Transactional(readOnly = true)
    public List<PostResponse> searchPosts(String keyword) {
        List<Post> posts = postRepository.findByTitleOrContentContaining(keyword);
        
        return posts.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }
    
    /**
     * 사용자별 게시글 조회
     */
    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + username));
        
        List<Post> posts = postRepository.findByAuthor(user);
        
        return posts.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }
} 