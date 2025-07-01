package com.example.totalweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // totalexam과 동일한 사용자들을 설정 (password123으로 통일)
        UserDetails admin = User.builder()
                .username("admin")
                .password("$2a$10$8t4h6q/QB8bqDt76d/ifjOYi71pPX/oupMrHzB3QX0ij.IloPxBLW") // password123
                .roles("ADMIN")
                .build();
        
        UserDetails user1 = User.builder()
                .username("user1")
                .password("$2a$10$8t4h6q/QB8bqDt76d/ifjOYi71pPX/oupMrHzB3QX0ij.IloPxBLW") // password123
                .roles("USER")
                .build();
        
        UserDetails user2 = User.builder()
                .username("user2")
                .password("$2a$10$8t4h6q/QB8bqDt76d/ifjOYi71pPX/oupMrHzB3QX0ij.IloPxBLW") // password123
                .roles("USER")
                .build();
        
        UserDetails john = User.builder()
                .username("john")
                .password("$2a$10$8t4h6q/QB8bqDt76d/ifjOYi71pPX/oupMrHzB3QX0ij.IloPxBLW") // password123
                .roles("USER")
                .build();
        
        UserDetails jane = User.builder()
                .username("jane")
                .password("$2a$10$8t4h6q/QB8bqDt76d/ifjOYi71pPX/oupMrHzB3QX0ij.IloPxBLW") // password123
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user1, user2, john, jane);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // API 호출을 위해 CSRF 비활성화
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/home", "/login", "/css/**", "/js/**", "/images/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );
        
        return http.build();
    }
} 