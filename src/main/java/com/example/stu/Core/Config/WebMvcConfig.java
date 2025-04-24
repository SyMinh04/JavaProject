package com.example.stu.Core.Config;

import com.example.stu.Core.Auth.JwtAuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for Spring MVC
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    private final JwtAuthInterceptor jwtAuthInterceptor;
    
    @Autowired
    public WebMvcConfig(JwtAuthInterceptor jwtAuthInterceptor) {
        this.jwtAuthInterceptor = jwtAuthInterceptor;
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                    "/api/users/login",
                    "/api/users/refresh-token",
                    "/api/users"
                );
        System.out.println("WebMvcConfig: JwtAuthInterceptor registered");
    }
}