package fr.waveme.backend.auth.crud.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestAuthController {
    @GetMapping("/auth")
    public ResponseEntity<String> testAuth() {
        return ResponseEntity.ok("Authentication successful!");
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        return "Public Endpoint: No authentication required!";
    }

    @GetMapping("/private")
    public String privateEndpoint() {
        return "Private Endpoint: Authentication required!";
    }
}
