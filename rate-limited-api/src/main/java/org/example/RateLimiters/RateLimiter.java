package org.example.RateLimiters;

import jakarta.servlet.http.HttpServletRequest;

public interface RateLimiter {
    public boolean allowRequest(HttpServletRequest request);
}
