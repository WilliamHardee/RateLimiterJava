package org.example.RateLimiters;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

public class LeakyBucketLimiter implements RateLimiter{
    private int bucketCapacity;
    private double leakRate;
    private BlockingQueue<String> bucket;
    private Map<String, Semaphore> semaphoreMap;

    public LeakyBucketLimiter(int bucketCapacity, double leakRate) {
        this.bucketCapacity = bucketCapacity;
        bucket = new ArrayBlockingQueue<>(bucketCapacity);
        semaphoreMap = new ConcurrentHashMap<>();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        long interval = (long) (1_000_000L / leakRate);
        scheduler.scheduleAtFixedRate(
                this::bucketConsumer,
                0,
                interval,
                TimeUnit.MICROSECONDS
        );

    }

    @Override
    public boolean allowRequest(HttpServletRequest request) {
        String uuid = UUID.randomUUID().toString();
        Semaphore semaphore = new Semaphore(0);
        semaphoreMap.put(uuid, semaphore);
        if(!bucket.offer(uuid)) {
            semaphoreMap.remove(uuid);
            return false;
        }
        try {
            semaphore.acquire();
        }
        catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    public void bucketConsumer() {

        String uuid = bucket.poll();
        if(uuid == null) {
            return;
        }
        semaphoreMap.get(uuid).release();
        semaphoreMap.remove(uuid);


    }

}
