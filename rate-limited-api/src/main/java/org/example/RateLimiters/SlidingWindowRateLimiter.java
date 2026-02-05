package org.example.RateLimiters;

import jakarta.servlet.http.HttpServletRequest;

public class SlidingWindowRateLimiter implements RateLimiter{
    @Override
    public boolean allowRequest(HttpServletRequest request) {
        return false;
    }
}
