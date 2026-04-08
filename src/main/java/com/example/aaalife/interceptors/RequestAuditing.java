package com.example.aaalife.interceptors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RequestAuditing implements HandlerInterceptor {
    private static final Logger logger = LogManager.getLogger(RequestAuditing.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String user = request.getRemoteUser() != null ? request.getRemoteUser() : "anonymous";
        logger.info("Incoming request: {} {} from user: {}", method, uri, user);
        return true;
    }
    
}
