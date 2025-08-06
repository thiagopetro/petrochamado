package com.lovablepetro.chamadopetro.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {
    
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bem-vindo ao Chamado Petro API");
        response.put("description", "Sistema de Gerenciamento de Tickets de Suporte Técnico");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now());
        response.put("status", "online");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("auth", "/api/auth");
        endpoints.put("users", "/api/users");
        endpoints.put("tickets", "/api/tickets");
        endpoints.put("test", "/api/test");
        
        response.put("endpoints", endpoints);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "Chamado Petro Backend");
        
        return ResponseEntity.ok(response);
    }
}