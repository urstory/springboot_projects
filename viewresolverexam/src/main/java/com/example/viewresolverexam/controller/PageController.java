package com.example.viewresolverexam.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.viewresolverexam.dto.User;

/**
 * ViewResolver를 사용하는 컨트롤러
 * - View 이름(문자열)을 반환하여 HTML 페이지로 변환
 * - Thymeleaf ViewResolver가 View 이름을 실제 HTML 파일로 매핑
 */
@Controller
@RequestMapping("/pages")
public class PageController {

    // 샘플 사용자 데이터
    private final List<User> users = List.of(
            new User(1L, "Alice", 25, "alice@example.com"),
            new User(2L, "Bob", 30, "bob@example.com"),
            new User(3L, "Charlie", 35, "charlie@example.com")
    );

    /**
     * 홈 페이지
     * ViewResolver 사용: "home" → /templates/home.html
     */
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("title", "ViewResolver 예제");
        model.addAttribute("message", "ViewResolver를 통해 HTML 페이지가 반환됩니다.");
        return "home"; // View 이름 반환 → ViewResolver가 처리
    }

    /**
     * 사용자 목록 페이지
     * ViewResolver 사용: "user-list" → /templates/user-list.html
     */
    @GetMapping("/users")
    public String userList(Model model) {
        model.addAttribute("users", users);
        model.addAttribute("title", "사용자 목록");
        return "user-list"; // View 이름 반환 → ViewResolver가 처리
    }

    /**
     * 사용자 상세 페이지
     * ViewResolver 사용: "user-detail" → /templates/user-detail.html
     */
    @GetMapping("/users/{id}")
    public String userDetail(@PathVariable Long id, Model model) {
        User user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
        
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("title", "사용자 상세정보");
            return "user-detail"; // View 이름 반환 → ViewResolver가 처리
        } else {
            model.addAttribute("error", "사용자를 찾을 수 없습니다.");
            return "error"; // View 이름 반환 → ViewResolver가 처리
        }
    }
} 