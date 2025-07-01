package com.example.beanexam.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.beanexam.component.CommonUtil;
import com.example.beanexam.model.MyEncoder;
import com.example.beanexam.model.MyService;
import com.example.beanexam.scope.PrototypeBean;
import com.example.beanexam.scope.RequestBean;
import com.example.beanexam.scope.SessionBean;
import com.example.beanexam.scope.SingletonBean;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 내용.md 5.1절과 5.2절 Bean 예제들을 테스트하는 컨트롤러
 * Bean 정의 방법과 Bean Scope 학습을 위한 API 제공
 */
@Slf4j
@RestController
@RequestMapping("/api/bean-examples")
@RequiredArgsConstructor
public class BeanExampleController {

    private final ApplicationContext context;
    private final MyService myService;
    private final MyEncoder myEncoder;
    private final CommonUtil commonUtil;
    private final SingletonBean singletonBean;
    private final RequestBean requestBean;
    private final SessionBean sessionBean;

    /**
     * 5.1절 - @Bean 방식 예제 테스트
     */
    @GetMapping("/bean-annotation")
    public Map<String, Object> testBeanAnnotation() {
        log.info("📋 @Bean 방식 예제 테스트");
        
        Map<String, Object> result = new HashMap<>();
        
        // MyService 테스트
        String serviceMessage = myService.getMessage();
        myService.printMessage(); // 콘솔 출력
        
        // MyEncoder 테스트
        String encodedData = myEncoder.encode("test123");
        
        result.put("method", "@Bean 어노테이션 방식");
        result.put("description", "AppConfig 클래스에서 @Bean 메서드로 정의된 Bean들");
        result.put("myService", Map.of(
            "message", serviceMessage,
            "hashCode", myService.hashCode()
        ));
        result.put("myEncoder", Map.of(
            "algorithm", myEncoder.getAlgorithm(),
            "encodedData", encodedData,
            "hashCode", myEncoder.hashCode()
        ));
        
        return result;
    }

    /**
     * 5.1절 - @Component 방식 예제 테스트
     */
    @GetMapping("/component-annotation")
    public Map<String, Object> testComponentAnnotation() {
        log.info("📋 @Component 방식 예제 테스트");
        
        Map<String, Object> result = new HashMap<>();
        
        // CommonUtil 테스트
        String uuid = commonUtil.generateUUID();
        boolean isValid = commonUtil.isValidString("test");
        long currentTime = commonUtil.getCurrentTimeMillis();
        int sum = commonUtil.sum(1, 2, 3, 4, 5);
        
        result.put("method", "@Component 어노테이션 방식");
        result.put("description", "Component Scan으로 자동 등록된 Bean");
        result.put("commonUtil", Map.of(
            "uuid", uuid,
            "isValidString", isValid,
            "currentTime", currentTime,
            "sum", sum,
            "hashCode", commonUtil.hashCode()
        ));
        
        return result;
    }

    /**
     * 5.2절 - Singleton Scope 테스트
     */
    @GetMapping("/singleton-scope")
    public Map<String, Object> testSingletonScope() {
        log.info("📋 Singleton Scope 테스트");
        
        Map<String, Object> result = new HashMap<>();
        
        // 여러 번 호출하여 같은 인스턴스임을 확인
        int count1 = singletonBean.incrementAndGet();
        int count2 = singletonBean.incrementAndGet();
        
        // ApplicationContext에서 직접 가져와서 비교
        SingletonBean contextBean = context.getBean(SingletonBean.class);
        int count3 = contextBean.incrementAndGet();
        
        result.put("scope", "Singleton (기본값)");
        result.put("description", "스프링 컨테이너당 단 하나의 Bean 인스턴스만 생성");
        result.put("sameInstance", singletonBean == contextBean);
        result.put("counts", Map.of(
            "first", count1,
            "second", count2,
            "fromContext", count3
        ));
        result.put("instanceInfo", singletonBean.getInstanceInfo());
        
        return result;
    }

    /**
     * 5.2절 - Prototype Scope 테스트
     */
    @GetMapping("/prototype-scope")
    public Map<String, Object> testPrototypeScope() {
        log.info("📋 Prototype Scope 테스트");
        
        Map<String, Object> result = new HashMap<>();
        
        // ApplicationContext에서 여러 번 가져와서 다른 인스턴스임을 확인
        PrototypeBean bean1 = context.getBean(PrototypeBean.class);
        PrototypeBean bean2 = context.getBean(PrototypeBean.class);
        
        int count1 = bean1.incrementAndGet();
        int count2 = bean2.incrementAndGet();
        
        result.put("scope", "Prototype");
        result.put("description", "Bean을 요청할 때마다 새로운 인스턴스를 생성");
        result.put("differentInstance", bean1 != bean2);
        result.put("bean1", Map.of(
            "count", count1,
            "instanceInfo", bean1.getInstanceInfo(),
            "createdTime", bean1.getCreatedTime()
        ));
        result.put("bean2", Map.of(
            "count", count2,
            "instanceInfo", bean2.getInstanceInfo(),
            "createdTime", bean2.getCreatedTime()
        ));
        
        return result;
    }

    /**
     * 5.2절 - Request Scope 테스트 (웹 환경 전용)
     */
    @GetMapping("/request-scope")
    public Map<String, Object> testRequestScope() {
        log.info("📋 Request Scope 테스트");
        
        Map<String, Object> result = new HashMap<>();
        
        // 같은 요청 내에서 여러 번 호출
        String data1 = requestBean.getData();
        String data2 = requestBean.getData();
        requestBean.setData("Modified Data");
        String data3 = requestBean.getData();
        
        result.put("scope", "Request");
        result.put("description", "HTTP 요청당 하나의 Bean 인스턴스가 생성");
        result.put("requestInfo", requestBean.getRequestInfo());
        result.put("dataAccess", Map.of(
            "first", data1,
            "second", data2,
            "afterModification", data3
        ));
        result.put("accessCount", requestBean.getAccessCount());
        
        return result;
    }

    /**
     * 5.2절 - Session Scope 테스트 (웹 환경 전용)
     */
    @GetMapping("/session-scope")
    public Map<String, Object> testSessionScope() {
        log.info("📋 Session Scope 테스트");
        
        Map<String, Object> result = new HashMap<>();
        
        // 세션 Bean에 아이템 추가
        sessionBean.addItem("사과");
        sessionBean.addItem("바나나");
        sessionBean.setUsername("testUser");
        
        result.put("scope", "Session");
        result.put("description", "HTTP 세션당 하나의 Bean 인스턴스를 생성하고 유지");
        result.put("sessionInfo", sessionBean.getSessionInfo());
        result.put("cart", sessionBean.getCart());
        result.put("cartSize", sessionBean.getCartSize());
        result.put("username", sessionBean.getUsername());
        
        return result;
    }

    /**
     * Session Scope - 장바구니 조작 API
     */
    @PostMapping("/session-scope/cart")
    public Map<String, Object> manipulateCart(@RequestParam String action, 
                                             @RequestParam(required = false) String item) {
        log.info("📋 Session Cart 조작: {} - {}", action, item);
        
        switch (action) {
            case "add":
                if (item != null) sessionBean.addItem(item);
                break;
            case "remove":
                if (item != null) sessionBean.removeItem(item);
                break;
            case "clear":
                sessionBean.clearCart();
                break;
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("action", action);
        result.put("cart", sessionBean.getCart());
        result.put("cartSize", sessionBean.getCartSize());
        result.put("sessionInfo", sessionBean.getSessionInfo());
        
        return result;
    }

    /**
     * 모든 Bean 예제 요약 정보
     */
    @GetMapping("/summary")
    public Map<String, Object> getSummary() {
        log.info("📋 Bean 예제 요약 정보");
        
        Map<String, Object> result = new HashMap<>();
        
        result.put("title", "내용.md 5.1절, 5.2절 Bean 예제 요약");
        result.put("beanDefinitionMethods", Map.of(
            "@Bean", "AppConfig 클래스에서 수동으로 Bean 정의",
            "@Component", "Component Scan으로 자동 Bean 등록",
            "@Service", "비즈니스 로직 처리 Bean",
            "@Repository", "데이터 접근 계층 Bean"
        ));
        result.put("beanScopes", Map.of(
            "Singleton", "컨테이너당 하나의 인스턴스 (기본값)",
            "Prototype", "요청할 때마다 새로운 인스턴스",
            "Request", "HTTP 요청당 하나의 인스턴스",
            "Session", "HTTP 세션당 하나의 인스턴스"
        ));
        result.put("testEndpoints", Map.of(
            "beanAnnotation", "/api/bean-examples/bean-annotation",
            "componentAnnotation", "/api/bean-examples/component-annotation",
            "singletonScope", "/api/bean-examples/singleton-scope",
            "prototypeScope", "/api/bean-examples/prototype-scope",
            "requestScope", "/api/bean-examples/request-scope",
            "sessionScope", "/api/bean-examples/session-scope"
        ));
        
        return result;
    }
} 