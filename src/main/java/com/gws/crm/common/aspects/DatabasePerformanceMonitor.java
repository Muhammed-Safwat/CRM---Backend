package com.gws.crm.common.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Aspect
@Component
public class DatabasePerformanceMonitor {

    private static final ConcurrentHashMap<String, AtomicLong> methodStats = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, AtomicLong> methodCounts = new ConcurrentHashMap<>();

    //   Repository methods
    @Around("execution(* com.gws.crm.*.repository.*.*(..))")
    public Object monitorRepositoryMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        return monitorMethod(joinPoint, "🗄️ DATABASE");
    }

    //   Service methods
    @Around("execution(* com.gws.crm.*.service.*.*(..))")
    public Object monitorServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        return monitorMethod(joinPoint, "⚙️ SERVICE");
    }

    //   Controller methods
    @Around("execution(* com.gws.crm.*.controller.*.*(..))")
    public Object monitorControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        return monitorMethod(joinPoint, "🌐 CONTROLLER");
    }

    private Object monitorMethod(ProceedingJoinPoint joinPoint, String layer) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String fullMethodName = className + "." + methodName;

        long startTime = System.currentTimeMillis();
        long startNano = System.nanoTime();

        try {
            log.debug("🚀 {} - {} STARTED", layer, fullMethodName);

            Object result = joinPoint.proceed();

            long duration = System.currentTimeMillis() - startTime;
            long durationNano = (System.nanoTime() - startNano) / 1_000_000;

            methodStats.computeIfAbsent(fullMethodName, k -> new AtomicLong(0)).addAndGet(duration);
            methodCounts.computeIfAbsent(fullMethodName, k -> new AtomicLong(0)).incrementAndGet();

            String performanceLevel = getPerformanceLevel(duration);

            log.info("✅ {} - {} COMPLETED | Duration: {}ms | Performance: {}",
                    layer, fullMethodName, duration, performanceLevel);

            if (duration > 500) {
                long avgDuration = methodStats.get(fullMethodName).get() / methodCounts.get(fullMethodName).get();
                log.warn("⚠️ SLOW {} METHOD: {} took {}ms (avg: {}ms) - INVESTIGATE!",
                        layer, fullMethodName, duration, avgDuration);
            }

            return result;

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("💥 {} - {} FAILED after {}ms | Error: {}",
                    layer, fullMethodName, duration, e.getMessage());
            throw e;
        }
    }

    private String getPerformanceLevel(long duration) {
        if (duration < 50) return "🟢 EXCELLENT";
        if (duration < 200) return "🟡 GOOD";
        if (duration < 500) return "🟠 ACCEPTABLE";
        if (duration < 2000) return "🔴 SLOW";
        return "💀 CRITICAL";
    }

}