package com.gws.crm.common.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class PerformanceAnalysisFilter implements Filter {

    private static final ConcurrentHashMap<String, AtomicInteger> activeRequests = new ConcurrentHashMap<>();
    private static final AtomicInteger totalActiveRequests = new AtomicInteger(0);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestId = generateRequestId();
        String endpoint = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        // تتبع الـ Active Requests
        int currentActive = totalActiveRequests.incrementAndGet();
        activeRequests.computeIfAbsent(endpoint, k -> new AtomicInteger(0)).incrementAndGet();

        long startTime = System.currentTimeMillis();
        long startNano = System.nanoTime();

        log.info("🚀 [{}] {} {} - STARTED | Active Requests: {} | Thread: {}",
                requestId, method, endpoint, currentActive, Thread.currentThread().getName());

        try {
            // تشغيل الـ request
            chain.doFilter(request, response);

        } finally {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            long durationNano = (System.nanoTime() - startNano) / 1_000_000; // Convert to ms

            // تقليل العداد
            totalActiveRequests.decrementAndGet();
            activeRequests.get(endpoint).decrementAndGet();

            // تحديد مستوى الأداء
            String performanceLevel = getPerformanceLevel(duration);
            String statusEmoji = getStatusEmoji(httpResponse.getStatus());

            log.info("{} [{}] {} {} - COMPLETED | Duration: {}ms ({}ns precision) | Status: {} | Performance: {} | Active: {}",
                    statusEmoji, requestId, method, endpoint, duration, durationNano,
                    httpResponse.getStatus(), performanceLevel, totalActiveRequests.get());

            // تحذير للـ requests البطيئة
            if (duration > 1000) {
                log.warn("⚠️ SLOW REQUEST DETECTED: [{}] {} {} took {}ms - INVESTIGATE IMMEDIATELY!",
                        requestId, method, endpoint, duration);
            }

            // تحذير للـ requests المتراكمة
            if (totalActiveRequests.get() > 10) {
                log.warn("⚠️ HIGH CONCURRENT REQUESTS: {} active requests - Possible bottleneck!",
                        totalActiveRequests.get());
            }
        }
    }

    private String generateRequestId() {
        return String.valueOf(System.nanoTime()).substring(8);
    }

    private String getPerformanceLevel(long duration) {
        if (duration < 100) return "🟢 EXCELLENT";
        if (duration < 500) return "🟡 GOOD";
        if (duration < 1000) return "🟠 ACCEPTABLE";
        if (duration < 3000) return "🔴 SLOW";
        return "💀 CRITICAL";
    }

    private String getStatusEmoji(int status) {
        if (status >= 200 && status < 300) return "✅";
        if (status >= 300 && status < 400) return "↩️";
        if (status >= 400 && status < 500) return "❌";
        return "💥";
    }
}