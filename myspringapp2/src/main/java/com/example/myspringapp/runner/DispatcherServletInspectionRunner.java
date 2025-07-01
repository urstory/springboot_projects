package com.example.myspringapp.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.http.server.PathContainer;

import java.util.Map;

@Component
public class DispatcherServletInspectionRunner implements CommandLineRunner {

    @Autowired
    private ApplicationContext context;

    @Override
    public void run(String... args) throws Exception {
        DispatcherServlet dispatcherServlet = context.getBean(DispatcherServlet.class);
        System.out.println("DispatcherServlet Bean: " + dispatcherServlet);

        ApplicationContext dispatcherContext = dispatcherServlet.getWebApplicationContext();

        Map<String, HandlerMapping> handlerMappings = dispatcherContext.getBeansOfType(HandlerMapping.class);
        System.out.println("\n--- HandlerMappings ---");
        handlerMappings.forEach((name, mapping) ->
                System.out.println("HandlerMapping Bean Name: " + name + ", Bean: " + mapping)
        );

        Map<String, HandlerAdapter> handlerAdapters = dispatcherContext.getBeansOfType(HandlerAdapter.class);
        System.out.println("\n--- HandlerAdapters ---");
        handlerAdapters.forEach((name, adapter) ->
                System.out.println("HandlerAdapter Bean Name: " + name + ", Bean: " + adapter)
        );

        Map<String, HttpMessageConverter> messageConverters = dispatcherContext.getBeansOfType(HttpMessageConverter.class);
        System.out.println("\n--- HttpMessageConverters ---");
        messageConverters.forEach((name, converter) ->
                System.out.println("MessageConverter Bean Name: " + name + ", Bean: " + converter)
        );

        simulateRequest(handlerMappings, handlerAdapters, "/users", "GET");
    }



    private void simulateRequest(Map<String, HandlerMapping> handlerMappings,
                                 Map<String, HandlerAdapter> handlerAdapters,
                                 String uri,
                                 String method) throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest(method, uri);

        HandlerExecutionChain chain = null;

        for (HandlerMapping mapping : handlerMappings.values()) {
            chain = mapping.getHandler(request);
            if (chain != null) {
                System.out.println("\nHandler found for URI [" + uri + "] using [" + mapping + "]");
                System.out.println("Handler: " + chain.getHandler());

                if (mapping instanceof RequestMappingInfoHandlerMapping infoMapping) {
                    Map<RequestMappingInfo, HandlerMethod> methods = infoMapping.getHandlerMethods();

                    System.out.println("\n--- RequestMappingInfo와 HandlerMethod 매핑 정보 ---");
                    methods.forEach((info, handlerMethod) -> {
                        if (info.getPathPatternsCondition() != null &&
                                info.getPathPatternsCondition().getPatterns().stream()
                                        .anyMatch(pattern -> pattern.matches(PathContainer.parsePath(uri))) &&
                                info.getMethodsCondition().getMethods().stream()
                                        .anyMatch(m -> m.name().equals(method))) {

                            System.out.println("RequestMappingInfo: " + info);
                            System.out.println("HandlerMethod: " + handlerMethod);
                            System.out.println("Controller Class: " + handlerMethod.getBeanType());
                            System.out.println("Method Name: " + handlerMethod.getMethod().getName());
                        }
                    });
                }
                break;
            }
        }

        if (chain == null) {
            System.out.println("\nNo Handler found for URI [" + uri + "]");
            return;
        }

        HandlerAdapter adapter = null;
        for (HandlerAdapter handlerAdapter : handlerAdapters.values()) {
            if (handlerAdapter.supports(chain.getHandler())) {
                adapter = handlerAdapter;
                System.out.println("\nSupported HandlerAdapter found: " + adapter);
                break;
            }
        }

        if (adapter == null) {
            System.out.println("\nNo suitable HandlerAdapter found for handler [" + chain.getHandler() + "]");
        } else {
            System.out.println("\nHandlerAdapter [" + adapter + "] can invoke the handler [" + chain.getHandler() + "]");
        }
    }
}
