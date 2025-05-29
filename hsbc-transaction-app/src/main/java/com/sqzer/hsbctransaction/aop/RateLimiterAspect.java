package com.sqzer.hsbctransaction.aop;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RateLimiterAspect {

    private final RateLimiter rateLimiter;

    public RateLimiterAspect(RateLimiterRegistry registry) {
        // 从配置中读取限流器，名字和 application.properties 保持一致
        this.rateLimiter = registry.rateLimiter("allEndpoints");
    }

    @Around("execution(public * com.sqzer.hsbctransaction.controller..*(..))")
    public Object aroundController(ProceedingJoinPoint pjp) throws Throwable {
        try {
            // 尝试执行限流
            return RateLimiter.decorateCheckedSupplier(rateLimiter, pjp::proceed).apply();
        } catch (RequestNotPermitted ex) {
            // 限流触发，返回 429 状态
            return ResponseEntity.status(429).body("请求过快，请稍后再试");
        }
    }
}