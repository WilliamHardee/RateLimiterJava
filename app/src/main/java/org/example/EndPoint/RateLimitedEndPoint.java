package org.example.EndPoint;

import org.example.RateLimiters.RateLimiter;

public class RateLimitedEndPoint implements EndPoint{
    private RateLimiter rateLimiter;
    private EndPoint endPoint;

    public RateLimitedEndPoint(RateLimiter rateLimiter, EndPoint endPoint) {
        this.rateLimiter = rateLimiter;
        this.endPoint = endPoint;
    }

    @Override
    public String request() {
        if(rateLimiter.allowRequest()) {
            return endPoint.request();
        }
        return "DENIED";
    }
}
