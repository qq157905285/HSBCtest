package com.sqzer.hsbctransaction.aop;

import com.sqzer.hsbctransaction.exception.CustomRateLimitException;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
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
            System.out.println("限流，抛出异常");
            // 正确抛出内置的限流异常
            throw new CustomRateLimitException("接口访问太频繁，请稍后再试");
        }
    }
}