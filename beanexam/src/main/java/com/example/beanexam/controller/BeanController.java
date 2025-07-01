package com.example.beanexam.controller;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.beanexam.config.AppConfig;
import com.example.beanexam.repository.UserRepository;
import com.example.beanexam.service.EmailService;
import com.example.beanexam.service.PrototypeService;
import com.example.beanexam.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Bean 사용 예시를 보여주는 컨트롤러
 * 다양한 의존성 주입 방법을 실습
 */
@Slf4j
@RestController
@RequestMapping("/api/beans")
@RequiredArgsConstructor  // 생성자 주입을 위한 Lombok 어노테이션
public class BeanController {
    
    // 생성자 주입 (권장 방식)
    private final UserService userService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final AppConfig.ConfigurationService configurationService;  // @Primary Bean 자동 주입
    private final DateTimeFormatter dateTimeFormatter;
    private final Map<String, String> applicationSettings;
    
    // 필드 주입 (권장하지 않음, 테스트용)
    @Autowired
    private ApplicationContext applicationContext;
    
    /**
     * Bean 정보 조회 API
     */
    @GetMapping("/info")
    public Map<String, Object> getBeanInfo() {
        log.info("Bean 정보 조회 요청");
        
        Map<String, Object> beanInfo = new HashMap<>();
        beanInfo.put("userService", userService.getUserServiceInfo());
        beanInfo.put("repository", userRepository.getRepositoryInfo());
        beanInfo.put("emailService", emailService.getEmailServiceStatus());
        beanInfo.put("configService", configurationService.getServiceInfo());
        beanInfo.put("applicationSettings", applicationSettings);
        
        return beanInfo;
    }
    
    /**
     * Prototype Bean 테스트 API
     */
    @GetMapping("/prototype-test")
    public Map<String, String> testPrototypeBean() {
        log.info("Prototype Bean 테스트 요청");
        
        // 매번 새로운 인스턴스를 받아옴
        PrototypeService instance1 = applicationContext.getBean(PrototypeService.class);
        PrototypeService instance2 = applicationContext.getBean(PrototypeService.class);
        PrototypeService instance3 = applicationContext.getBean(PrototypeService.class);
        
        Map<String, String> result = new HashMap<>();
        result.put("instance1", instance1.getInstanceInfo());
        result.put("instance2", instance2.getInstanceInfo());
        result.put("instance3", instance3.getInstanceInfo());
        result.put("task1", instance1.performTask("첫 번째 작업"));
        result.put("task2", instance2.performTask("두 번째 작업"));
        result.put("task3", instance3.performTask("세 번째 작업"));
        
        return result;
    }
    
    /**
     * 사용자 관련 API
     */
    @GetMapping("/users")
    public List<UserRepository.UserData> getAllUsers() {
        log.info("모든 사용자 조회 요청");
        return userRepository.findAll();
    }
    
    @PostMapping("/users")
    public UserRepository.UserData createUser(@RequestParam String name,
                                              @RequestParam String email,
                                              @RequestParam String department) {
        log.info("사용자 생성 요청: name={}, email={}, department={}", name, email, department);
        
        UserRepository.UserData userData = new UserRepository.UserData(name, email, department);
        UserRepository.UserData savedUser = userRepository.save(userData);
        
        // 이메일 발송 (시뮬레이션)
        emailService.sendEmail(email, "계정 생성 완료", "안녕하세요! 계정이 성공적으로 생성되었습니다.");
        
        return savedUser;
    }
    
    @GetMapping("/users/{id}")
    public UserRepository.UserData getUser(@PathVariable Long id) {
        log.info("사용자 조회 요청: id={}", id);
        return userRepository.findById(id);
    }
    
    @DeleteMapping("/users/{id}")
    public Map<String, Object> deleteUser(@PathVariable Long id) {
        log.info("사용자 삭제 요청: id={}", id);
        
        boolean deleted = userRepository.deleteById(id);
        
        Map<String, Object> result = new HashMap<>();
        result.put("deleted", deleted);
        result.put("message", deleted ? "사용자가 삭제되었습니다." : "사용자를 찾을 수 없습니다.");
        
        return result;
    }
    
    /**
     * Bean Scope 비교 API
     */
    @GetMapping("/scope-comparison")
    public Map<String, Object> compareBeanScopes() {
        log.info("Bean Scope 비교 요청");
        
        // Singleton Bean - 항상 같은 인스턴스
        UserService singleton1 = applicationContext.getBean(UserService.class);
        UserService singleton2 = applicationContext.getBean(UserService.class);
        
        // Prototype Bean - 매번 새로운 인스턴스
        PrototypeService prototype1 = applicationContext.getBean(PrototypeService.class);
        PrototypeService prototype2 = applicationContext.getBean(PrototypeService.class);
        
        Map<String, Object> result = new HashMap<>();
        result.put("singletonSame", singleton1 == singleton2);
        result.put("singletonHashCode1", singleton1.hashCode());
        result.put("singletonHashCode2", singleton2.hashCode());
        result.put("prototypeSame", prototype1 == prototype2);
        result.put("prototypeHashCode1", prototype1.hashCode());
        result.put("prototypeHashCode2", prototype2.hashCode());
        result.put("explanation", Map.of(
            "singleton", "항상 같은 인스턴스를 반환 (hashCode 동일)",
            "prototype", "매번 새로운 인스턴스를 생성 (hashCode 다름)"
        ));
        
        return result;
    }
} 