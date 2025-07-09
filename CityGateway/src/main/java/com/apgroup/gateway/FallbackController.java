package com.apgroup.gateway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Louis Figes (w21017657)
 * This class is a fallback controller for the City Gateway application
 */
@RestController
public class FallbackController {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @RequestMapping("/newcastle-fallback")
    public ResponseEntity<FallbackResponse> newcastleFallback() {
        FallbackResponse response = new FallbackResponse(
                "Newcastle is currently unavailable. Please try again later.",
                LocalDateTime.now().format(formatter)
        );
        return ResponseEntity.status(503).body(response);
    }

    @RequestMapping("/sunderland-fallback")
    public ResponseEntity<FallbackResponse> sunderlandFallback() {
        FallbackResponse response = new FallbackResponse(
                "Sunderland is currently unavailable. Please try again later.",
                LocalDateTime.now().format(formatter)
        );
        return ResponseEntity.status(503).body(response);
    }

    @RequestMapping("/durham-fallback")
    public ResponseEntity<FallbackResponse> durhamFallback() {
        FallbackResponse response = new FallbackResponse(
                "Durham is currently unavailable. Please try again later.",
                LocalDateTime.now().format(formatter)
        );
        return ResponseEntity.status(503).body(response);
    }

    @RequestMapping("/darlington-fallback")
    public ResponseEntity<FallbackResponse> darlingtonFallback() {
        FallbackResponse response = new FallbackResponse(
                "Darlington is currently unavailable. Please try again later.",
                LocalDateTime.now().format(formatter)
        );
        return ResponseEntity.status(503).body(response);
    }

    @RequestMapping("/auth-fallback")
    public ResponseEntity<FallbackResponse> authFallback() {
        FallbackResponse response = new FallbackResponse(
                "Authentication service is currently unavailable. Please try again later.",
                LocalDateTime.now().format(formatter)
        );
        return ResponseEntity.status(503).body(response);
    }
}
