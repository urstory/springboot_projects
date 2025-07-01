package com.example.totalweb.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Value("${app.api.base-url}")
    private String apiBaseUrl;

    @GetMapping({"/", "/home"})
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("apiBaseUrl", apiBaseUrl);
        return "dashboard";
    }

    @GetMapping("/posts")
    public String posts(Model model) {
        model.addAttribute("apiBaseUrl", apiBaseUrl);
        return "posts";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("apiBaseUrl", apiBaseUrl);
        return "users";
    }
} 