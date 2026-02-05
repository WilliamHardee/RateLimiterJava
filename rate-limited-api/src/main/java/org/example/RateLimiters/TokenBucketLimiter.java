package org.example.RateLimiters;

import jakarta.servlet.http.HttpServletRequest;

public class TokenBucketLimiter implements RateLimiter {
    private long bucketCapacity;
    private int refillRate;
    private long curBucketSize;
    private long lastRefillTime;


    public TokenBucketLimiter(int bucketCapacity, int refillRate) {
        this.bucketCapacity = bucketCapacity;
        this.curBucketSize = bucketCapacity;
        this.refillRate = refillRate;
        this.lastRefillTime = System.currentTimeMillis();
    }

    @Override
    public synchronized boolean allowRequest(HttpServletRequest request) {
        long currentTime = System.currentTimeMillis();
        long elapsedMillis = currentTime - lastRefillTime;
        long tokensToBeAdded = (elapsedMillis * refillRate) / 1000;

        if(tokensToBeAdded >= 1) {
            curBucketSize = Math.min(bucketCapacity, curBucketSize + tokensToBeAdded);
            lastRefillTime += (tokensToBeAdded * 1000) / refillRate;
        }

        if(curBucketSize == 0) {
            return false;
        }

        curBucketSize -= 1;
        return true;
    }
}
