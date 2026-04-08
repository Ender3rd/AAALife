package com.example.aaalife.interceptors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ResultAuditing implements HandlerInterceptor {
    public static final Logger logger = LogManager.getLogger(ResultAuditing.class);

    // For simplicity, we will just log the response status code and the user. In a real application, you might want to log more details or use a more sophisticated approach.
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String user = SecurityContextHolder.getContext().getAuthentication() != null ? SecurityContextHolder.getContext().getAuthentication().getName() : "anonymous";
        int status = response.getStatus();
        logger.info("Response status: {} for user: {}", status, user);
        // TODO, send this as an event to Kafka or another event bus for integration with payments, underwriting, fraud detection, etc.
    }
}