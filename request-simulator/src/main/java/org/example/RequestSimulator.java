package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RequestSimulator {
    private static HttpClient client = HttpClient.newHttpClient();
    private int requestsPerSecond;
    private ScheduledExecutorService scheduler;

    public RequestSimulator(int requestsPerSecond, int numberOfThreads) {
        this.requestsPerSecond = requestsPerSecond;
        scheduler = Executors.newScheduledThreadPool(numberOfThreads);
    }

    public void startRequests() {
        scheduler.scheduleAtFixedRate(this::request, 0, 1000000 / requestsPerSecond, TimeUnit.MICROSECONDS);
    }

    public void request() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/ping"))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

}
