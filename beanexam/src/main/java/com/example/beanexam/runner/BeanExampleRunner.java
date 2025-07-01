package com.example.beanexam.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.example.beanexam.component.CommonUtil;
import com.example.beanexam.model.MyEncoder;
import com.example.beanexam.model.MyService;
import com.example.beanexam.scope.PrototypeBean;
import com.example.beanexam.scope.SingletonBean;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 내용.md 5.1절과 5.2절 Bean 예제들을 콘솔에서 실행하는 Runner
 * 애플리케이션 시작 시 Bean 정의 방법과 Bean Scope 예제를 보여줍니다.
 * 
 * 참고: Request/Session Scope Bean들은 웹 환경에서만 동작하므로
 * 웹 API를 통해서만 테스트할 수 있습니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BeanExampleRunner implements CommandLineRunner {

    private final ApplicationContext context;
    private final MyService myService;
    private final MyEncoder myEncoder;
    private final CommonUtil commonUtil;
    private final SingletonBean singletonBean;

    @Override
    public void run(String... args) throws Exception {
        log.info("🚀 내용.md 5.1절, 5.2절 Bean 예제 실행 시작");
        
        printSeparator("5.1절 - Bean 정의 방법 예제");
        testBeanDefinitionMethods();
        
        printSeparator("5.2절 - Bean Scope 예제 (Console)");
        testBeanScopes();
        
        log.info("🎯 내용.md Bean 예제 실행 완료");
        log.info("🌐 웹 API 테스트는 http://localhost:8080/api/bean-examples/summary 에서 확인하세요");
        log.info("📋 Request/Session Scope는 웹 환경에서만 동작하므로 웹 API를 통해 테스트하세요");
    }

    /**
     * 5.1절 - Bean 정의 방법 테스트
     */
    private void testBeanDefinitionMethods() {
        log.info("📋 1. @Bean 어노테이션 방식 테스트");
        
        // MyService 테스트
        log.info("   - MyService: {}", myService.getMessage());
        myService.printMessage();
        
        // MyEncoder 테스트
        String encoded = myEncoder.encode("hello world");
        log.info("   - MyEncoder: {} -> {}", myEncoder.getAlgorithm(), encoded);
        
        log.info("📋 2. @Component 어노테이션 방식 테스트");
        
        // CommonUtil 테스트
        String uuid = commonUtil.generateUUID();
        boolean isValid = commonUtil.isValidString("test");
        int sum = commonUtil.sum(1, 2, 3, 4, 5);
        
        log.info("   - CommonUtil UUID: {}", uuid);
        log.info("   - CommonUtil 문자열 검증: {}", isValid);
        log.info("   - CommonUtil 합계: {}", sum);
    }

    /**
     * 5.2절 - Bean Scope 테스트 (콘솔 전용)
     */
    private void testBeanScopes() {
        log.info("📋 1. Singleton Scope 테스트");
        
        // 주입받은 Bean과 Context에서 가져온 Bean이 같은지 확인
        SingletonBean contextBean = context.getBean(SingletonBean.class);
        boolean isSame = singletonBean == contextBean;
        
        log.info("   - 주입받은 Bean과 Context Bean이 같은 인스턴스인가? {}", isSame);
        log.info("   - 주입받은 Bean 해시코드: {}", singletonBean.hashCode());
        log.info("   - Context Bean 해시코드: {}", contextBean.hashCode());
        
        // 상태 공유 테스트
        int count1 = singletonBean.incrementAndGet();
        int count2 = contextBean.incrementAndGet();
        log.info("   - 첫 번째 호출 결과: {}", count1);
        log.info("   - 두 번째 호출 결과: {} (상태가 공유됨)", count2);
        
        log.info("📋 2. Prototype Scope 테스트");
        
        // 여러 번 Bean을 요청하여 다른 인스턴스임을 확인
        PrototypeBean proto1 = context.getBean(PrototypeBean.class);
        PrototypeBean proto2 = context.getBean(PrototypeBean.class);
        
        boolean isDifferent = proto1 != proto2;
        log.info("   - 두 번의 요청으로 얻은 Bean이 다른 인스턴스인가? {}", isDifferent);
        log.info("   - 첫 번째 Bean 해시코드: {}", proto1.hashCode());
        log.info("   - 두 번째 Bean 해시코드: {}", proto2.hashCode());
        
        // 독립적인 상태 테스트
        int protoCount1 = proto1.incrementAndGet();
        int protoCount2 = proto2.incrementAndGet();
        log.info("   - 첫 번째 Bean 카운트: {}", protoCount1);
        log.info("   - 두 번째 Bean 카운트: {} (독립적인 상태)", protoCount2);
        
        log.info("📋 3. Request/Session Scope는 웹 환경에서만 동작");
        log.info("   - Request Scope: HTTP 요청당 하나의 인스턴스");
        log.info("   - Session Scope: HTTP 세션당 하나의 인스턴스");
        log.info("   - 웹 API를 통해 테스트해보세요!");
        log.info("     GET http://localhost:8080/api/bean-examples/request-scope");
        log.info("     GET http://localhost:8080/api/bean-examples/session-scope");
    }

    private void printSeparator(String title) {
        log.info("");
        log.info("=" + "=".repeat(50));
        log.info("  {}", title);
        log.info("=" + "=".repeat(50));
    }
} 