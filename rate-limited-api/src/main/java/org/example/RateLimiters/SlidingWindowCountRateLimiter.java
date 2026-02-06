package org.example.RateLimiters;

import jakarta.servlet.http.HttpServletRequest;

public class SlidingWindowCountRateLimiter implements RateLimiter{
    private int windowCapacity;
    private long windowSizeMillis;
    private int previousWindowCount;
    private int currentWindowCount;
    private long previousWindowTime;

    public SlidingWindowCountRateLimiter(double windowSizeSecs, int windowCapacity) {
        this.windowCapacity = windowCapacity;
        this.windowSizeMillis = (long) (windowSizeSecs * 1000);
        this.previousWindowCount = 0;
        this.currentWindowCount = 0;
        long now = System.currentTimeMillis();
        this.previousWindowTime = now - (now % windowSizeMillis);
    }


    @Override
    public synchronized boolean allowRequest(HttpServletRequest request) {
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - previousWindowTime;
        if(timeDifference >= windowSizeMillis) {
            if(timeDifference >= windowSizeMillis * 2) {
                previousWindowCount = 0;
            }
            else {
                previousWindowCount = currentWindowCount;
            }
            currentWindowCount = 0;
            previousWindowTime = currentTime - (currentTime % windowSizeMillis);

        }

        double weight = (1 -((currentTime - previousWindowTime) / (double) windowSizeMillis)) * previousWindowCount + currentWindowCount;
        if(weight + 1 > windowCapacity) {
            return false;
        }
        currentWindowCount += 1;

        return true;
    }
}
