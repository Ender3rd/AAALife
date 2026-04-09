package com.example.aaalife.interceptors;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public abstract class RateLimiter implements HandlerInterceptor {

    // All the atomics and conncurrents are to make this thread safe while keeping locks for the shortest time possible. In a real application we would probably offload this to an HTTP proxy or a distributed cache.
    @SuppressWarnings("unchecked")
    private static final AtomicReference<ConcurrentHashMap<String, AtomicInteger>>[] userAttempts = new AtomicReference[4];
    private static final AtomicInteger DEFAULT_ZERO = new AtomicInteger(0); // DO NOT MUTATE THIS OR LET IT INTO THE MAPS TO BE MUTATED
    private static final int MAX_RATE = 25; // max attempts per time window. In a real application, this would be configurable.
    static {
        for (int i = 0; i < userAttempts.length; i++) {
            userAttempts[i] = new AtomicReference<>(new ConcurrentHashMap<>());
        }
    }

    @Scheduled(fixedRate = 60 * 1000)
    public void rotateAttemptCounts() {
        // Gotta love VS code for the handy auto generation. The auto-generated comments suggested it is a monstrosity. I can't say I disagree, but it does the job.
        userAttempts[3].getAndSet(userAttempts[2].getAndSet(userAttempts[1].getAndSet(userAttempts[0].getAndSet(new ConcurrentHashMap<>()))));
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String key = getKey(request);
        int attempt = userAttempts[0].get().compute(key, (k, currentValue) -> { 
            if (currentValue == null) {
                return new AtomicInteger(1);
            }
            currentValue.incrementAndGet();
            return currentValue;
        }
        ).get();
        // unrolling the loop for performance
        long additionalAttempts = userAttempts[1].get().getOrDefault(key, DEFAULT_ZERO).get() + userAttempts[2].get().getOrDefault(key, DEFAULT_ZERO).get() + userAttempts[3].get().getOrDefault(key, DEFAULT_ZERO).get() ;
        if (attempt + additionalAttempts > MAX_RATE) {
            response.setStatus(529); // Being nice to possible legitimate users. In a real application, this might have to be 401 or 403, but that would make testing and legitimate use more difficult.
            // maybe leave it as the result from the app, but have a proxy change the response. OTOH, we would probably just use the proxy to do this anyway.
            return false;
        }
        return true;
    }

    protected abstract String getKey(HttpServletRequest request);

}