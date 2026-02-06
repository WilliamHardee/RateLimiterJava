package org.example.RateLimiters;

import jakarta.servlet.http.HttpServletRequest;

import java.util.LinkedList;

public class SlidingWindowLogRateLimiter implements RateLimiter{
    private long windowSizeMillis;
    private long windowCapacity;
    private LinkedList<Long> window;

    public SlidingWindowLogRateLimiter(double windowSizeSecs, long windowCapacity) {
        this.windowSizeMillis = (long) (windowSizeSecs * 1000);
        this.windowCapacity = windowCapacity;
        this.window = new LinkedList<>();
    }

    @Override
    public synchronized boolean allowRequest(HttpServletRequest request) {
        long currentTime = System.currentTimeMillis();
        long minTimeAllowed = currentTime - windowSizeMillis;

        while(!window.isEmpty() && window.peekFirst() < minTimeAllowed) {
            window.pollFirst();
        }

        if(window.size() < windowCapacity) {
            window.addLast(currentTime);
            return true;
        }

        return false;
    }
}
