package com.sqzer.hsbctransaction.aop;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component("customRateLimiterAspect")
public class RateLimiterAspect {

    private final RateLimiterRegistry rateLimiterRegistry;

    public RateLimiterAspect(RateLimiterRegistry rateLimiterRegistry) {
        this.rateLimiterRegistry = rateLimiterRegistry;
    }

    @Around("execution(* com.sqzer.hsbctransaction.controller..*(..))")
    public Object limit(ProceedingJoinPoint joinPoint) throws Throwable {
        RateLimiter limiter = rateLimiterRegistry.rateLimiter("default");

        if (limiter.acquirePermission()) {
            return joinPoint.proceed();
        } else {
            // 正确抛出内置的限流异常
            return ResponseEntity.status(429).body("接口访问太频繁，请稍后再试");
        }
    }
}