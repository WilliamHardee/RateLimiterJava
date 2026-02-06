package org.example;

import jakarta.servlet.http.HttpServletRequest;
import org.example.RateLimiters.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EndPoint {

    private static final Logger log = LoggerFactory.getLogger(EndPoint.class);
    private final RateLimiter rateLimiter = new SlidingWindowCountRateLimiter(1, 4);

    @GetMapping("/ping")
    public ResponseEntity ping(HttpServletRequest request) {
        log.info("got request");
        if(rateLimiter.allowRequest(request)) {
            log.info("Request Allowed");
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(429)).build();
    }
}
