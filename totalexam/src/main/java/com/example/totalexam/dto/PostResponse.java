package com.example.totalexam.dto;

import java.time.LocalDateTime;

import com.example.totalexam.entity.Post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 게시글 응답 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    
    private Long id;
    private String title;
    private String content;
    private String authorUsername;
    private String authorName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.authorUsername = post.getAuthor().getUsername();
        this.authorName = post.getAuthor().getFullName();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
} 