package com.example.aaalife.interceptors;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

// Prevent abusive users from overwhelming the system with requests.
// logic is deliberately bad for demonstration purposes. In a real application, this would not be done by a spring service but by a proxy or via a distributed cache.
@Component
public class IPBasedRateLimiter extends RateLimiter {
    @Override
    protected String getKey(HttpServletRequest request) {
        return request.getRemoteAddr();
    }
}