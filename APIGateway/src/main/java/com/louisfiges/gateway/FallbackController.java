package com.louisfiges.gateway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class FallbackController {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @RequestMapping("/citizen-fallback")
    public ResponseEntity<FallbackResponse> citizenFallback() {
        FallbackResponse response = new FallbackResponse(
                "Citizen service is currently unavailable. Please try again later.",
                LocalDateTime.now().format(formatter)
        );
        return ResponseEntity.status(503).body(response);
    }

    @RequestMapping("/provider-a-fallback")
    public ResponseEntity<FallbackResponse> providerAFallback() {
        FallbackResponse response = new FallbackResponse(
                "Provider A service is currently unavailable. Please try again later.",
                LocalDateTime.now().format(formatter)
        );
        return ResponseEntity.status(503).body(response);
    }

    @RequestMapping("/provider-b-fallback")
    public ResponseEntity<FallbackResponse> providerBFallback() {
        FallbackResponse response = new FallbackResponse(
                "Provider B service is currently unavailable. Please try again later.",
                LocalDateTime.now().format(formatter)
        );
        return ResponseEntity.status(503).body(response);
    }

    @RequestMapping("/provider-c-fallback")
    public ResponseEntity<FallbackResponse> providerCFallback() {
        FallbackResponse response = new FallbackResponse(
                "Provider C service is currently unavailable. Please try again later.",
                LocalDateTime.now().format(formatter)
        );
        return ResponseEntity.status(503).body(response);
    }

    @RequestMapping("/smart-city-fallback")
    public ResponseEntity<FallbackResponse> smartCityFallback() {
        FallbackResponse response = new FallbackResponse(
                "Smart City service is currently unavailable. Please try again later.",
                LocalDateTime.now().format(formatter)
        );
        return ResponseEntity.status(503).body(response);
    }
}
