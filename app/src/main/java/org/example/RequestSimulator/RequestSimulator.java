package org.example.RequestSimulator;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RequestSimulator {
    private final int requestsPerSecond;
    ScheduledExecutorService scheduledExecutorService;

    public RequestSimulator(int requestsPerSecond, int numberOfThreads) {
        if(requestsPerSecond <= 0 || requestsPerSecond > 1000 ) throw new IllegalArgumentException("Invalid Number of Requests Per Seconds");
        if(numberOfThreads <= 0 || numberOfThreads > 10) throw new IllegalArgumentException("Invalid Number of Threads");
        this.requestsPerSecond = requestsPerSecond;
        this.scheduledExecutorService = Executors.newScheduledThreadPool(numberOfThreads);
    }

    public void startRequests() {
        scheduledExecutorService.scheduleAtFixedRate(new Requester(), 0, 1_000_000L / requestsPerSecond, TimeUnit.MICROSECONDS);
    }

    public void stopRequests() {
        scheduledExecutorService.shutdown();
        System.out.println("ALl requesters shut down");
    }

}
