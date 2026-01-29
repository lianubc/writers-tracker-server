package com.writerstracker.server.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(1) // Runs before security
public class RateLimitFilter implements Filter {

    // Store: IP Address -> (Request Count, Last Request Time)
    private final Map<String, UserRequestHistory> requestCounts = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String clientIp = req.getRemoteAddr();

        // LOGIC: Allow max 10 requests per 60 seconds (1 minute)
        long currentTime = System.currentTimeMillis();
        requestCounts.putIfAbsent(clientIp, new UserRequestHistory(0, currentTime));
        UserRequestHistory history = requestCounts.get(clientIp);

        // Reset counter if 1 minute has passed
        if (currentTime - history.startTime > 60000) {
            history.startTime = currentTime;
            history.count = 0;
        }

        // Check limit
        if (history.count >= 10) { // <--- LIMIT IS HERE
            res.setStatus(429); // HTTP 429: Too Many Requests
            res.getWriter().write("Too many requests! Please wait.");
            return; // Stop processing
        }

        // Increment and proceed
        history.count++;
        chain.doFilter(request, response);
    }

    // Helper class to store count and time
    private static class UserRequestHistory {
        int count;
        long startTime;

        UserRequestHistory(int count, long startTime) {
            this.count = count;
            this.startTime = startTime;
        }
    }
}
