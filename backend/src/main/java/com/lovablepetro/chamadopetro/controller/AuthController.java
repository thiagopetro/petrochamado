package com.lovablepetro.chamadopetro.controller;

import com.lovablepetro.chamadopetro.dto.AuthResponseDTO;
import com.lovablepetro.chamadopetro.dto.LoginRequestDTO;
import com.lovablepetro.chamadopetro.dto.RegisterRequestDTO;
import com.lovablepetro.chamadopetro.entity.User;
import com.lovablepetro.chamadopetro.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        try {
            AuthResponseDTO response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        try {
            AuthResponseDTO response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = authService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/technicians")
    public ResponseEntity<List<String>> getTechnicians() {
        List<String> technicians = authService.getTechnicianNames();
        return ResponseEntity.ok(technicians);
    }
}

@RestController
@RequestMapping("/api/setup")
@CrossOrigin(origins = "*")
class SetupController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/superuser")
    public ResponseEntity<Map<String, String>> createSuperUser(@Valid @RequestBody RegisterRequestDTO request) {
        try {
            authService.createSuperUser(request.getUsername(), request.getEmail(), request.getPassword(), request.getFullName());
            return ResponseEntity.ok(Map.of("message", "Superusuário criado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkSetup() {
        boolean hasUsers = authService.hasUsers();
        return ResponseEntity.ok(Map.of("hasUsers", hasUsers));
    }
}