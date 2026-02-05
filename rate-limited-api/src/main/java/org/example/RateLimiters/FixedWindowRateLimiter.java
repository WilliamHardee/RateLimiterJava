package org.example.RateLimiters;

import jakarta.servlet.http.HttpServletRequest;

public class FixedWindowRateLimiter implements RateLimiter{
    private long windowSizeMilli;
    private int windowCapacity;
    private  long currWindow = System.currentTimeMillis();
    private long currWindowRequests;

    public FixedWindowRateLimiter(double windowSizeSecs, int windowCapacity) {
        this.windowSizeMilli =(long) (windowSizeSecs * 1000);
        this.windowCapacity = windowCapacity;
        this.currWindowRequests = 0;
    }

    @Override
    public synchronized  boolean allowRequest(HttpServletRequest request) {
        if(System.currentTimeMillis() - currWindow >= windowSizeMilli) {
            currWindowRequests = 0;
            currWindow = System.currentTimeMillis();
        }
        currWindowRequests += 1;
        return currWindowRequests <= windowCapacity;
    }

}
