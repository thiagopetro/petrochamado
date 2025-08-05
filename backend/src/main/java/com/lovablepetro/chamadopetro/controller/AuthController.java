package com.lovablepetro.chamadopetro.controller;

import com.lovablepetro.chamadopetro.dto.AuthResponseDTO;
import com.lovablepetro.chamadopetro.dto.LoginRequestDTO;
import com.lovablepetro.chamadopetro.dto.RegisterRequestDTO;
import com.lovablepetro.chamadopetro.dto.UserResponseDTO;
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
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody(required = false) LoginRequestDTO request) {
        System.err.println("=== LOGIN ENDPOINT REACHED ===");
        System.err.println("=== LOGIN ATTEMPT START ===");
        System.err.println("Request object: " + request);
        System.err.println("User: " + (request != null ? request.getUsername() : "null request"));
        System.err.println("Password length: " + (request != null && request.getPassword() != null ? request.getPassword().length() : "null password"));
        
        if (request == null) {
            System.err.println("=== REQUEST IS NULL ===");
            return ResponseEntity.badRequest().body("Request body is null");
        }
        
        try {
            AuthResponseDTO response = authService.login(request);
            System.err.println("=== LOGIN SUCCESS ===");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("=== LOGIN ERROR ===");
            System.err.println("Exception: " + e.getClass().getName());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace(System.err);
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
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = authService.getAllUsers();
        List<UserResponseDTO> userDTOs = users.stream()
                .map(UserResponseDTO::new)
                .toList();
        return ResponseEntity.ok(userDTOs);
    }
    
    @GetMapping("/technicians")
    public ResponseEntity<List<String>> getTechnicians() {
        List<String> technicians = authService.getTechnicianNames();
        return ResponseEntity.ok(technicians);
    }
    
    @PostMapping("/test-login")
    public ResponseEntity<?> testLogin(@RequestBody LoginRequestDTO request) {
        try {
            System.err.println("=== TEST LOGIN ===");
            System.err.println("Username: " + request.getUsername());
            System.err.println("Password: " + request.getPassword());
            
            // Verificar se usuário existe
            var user = authService.getUserByUsername(request.getUsername());
            if (user.isEmpty()) {
                return ResponseEntity.ok(Map.of("error", "Usuário não encontrado"));
            }
            
            System.err.println("User found: " + user.get().getUsername());
            System.err.println("Stored password: " + user.get().getPassword());
            
            // Testar verificação de senha
            boolean passwordMatch = authService.checkPassword(request.getPassword(), user.get().getPassword());
            System.err.println("Password match: " + passwordMatch);
            
            if (!passwordMatch) {
                return ResponseEntity.ok(Map.of("error", "Senha incorreta"));
            }
            
            return ResponseEntity.ok(Map.of("success", "Credenciais válidas"));
            
        } catch (Exception e) {
            System.err.println("Test login error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(Map.of("error", e.getMessage()));
        }
    }
}

@RestController
@RequestMapping("/api/setup")
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