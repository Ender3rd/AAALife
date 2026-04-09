package com.example.aaalife.interceptors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RequestAuditing implements HandlerInterceptor {
    private static final Logger logger = LogManager.getLogger(RequestAuditing.class);
    private static final Marker AUDIT_MARKER = MarkerManager.getMarker("AUDIT");

    // Log major changes to the audit subsystem. Logging all requests should be done by a dedicated analytics or observability service. Promethius for example.
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!request.getMethod().equalsIgnoreCase("POST")) {
            return true;
        }
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String user = SecurityContextHolder.getContext().getAuthentication() != null ? SecurityContextHolder.getContext().getAuthentication().getName() : "anonymous";
        logger.info(AUDIT_MARKER, "Incoming request: {} {} from user: {}", method, uri, user);
        return true;
    }
    
}
