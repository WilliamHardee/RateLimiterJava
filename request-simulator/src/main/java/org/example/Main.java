package org.example;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        RequestSimulator simulator = new RequestSimulator(2, 4);
        simulator.startRequests();
    }

}