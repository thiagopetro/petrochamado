package com.lovablepetro.chamadopetro.controller;

import com.lovablepetro.chamadopetro.dto.LoginRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        System.err.println("=== TEST ENDPOINT CALLED ===");
        return ResponseEntity.ok("Hello World!");
    }
    
    @PostMapping("/test-post")
    public ResponseEntity<String> testPost(@RequestBody(required = false) String body) {
        System.err.println("=== TEST POST ENDPOINT CALLED ===");
        System.err.println("Body: " + body);
        return ResponseEntity.ok("POST received: " + body);
    }
    
    @PostMapping("/test-login")
    public ResponseEntity<String> testLogin(@RequestBody(required = false) LoginRequestDTO request) {
        System.err.println("=== TEST LOGIN ENDPOINT CALLED ===");
        System.err.println("Request: " + request);
        System.err.println("Username: " + (request != null ? request.getUsername() : "null"));
        System.err.println("Password: " + (request != null ? request.getPassword() : "null"));
        return ResponseEntity.ok("Login test received: " + (request != null ? request.getUsername() : "null"));
    }
}