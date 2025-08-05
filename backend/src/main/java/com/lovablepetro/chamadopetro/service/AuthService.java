package com.lovablepetro.chamadopetro.service;

import com.lovablepetro.chamadopetro.dto.AuthResponseDTO;
import com.lovablepetro.chamadopetro.dto.LoginRequestDTO;
import com.lovablepetro.chamadopetro.dto.RegisterRequestDTO;
import com.lovablepetro.chamadopetro.entity.Role;
import com.lovablepetro.chamadopetro.entity.User;
import com.lovablepetro.chamadopetro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtService jwtService;
    

    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }
    
    public AuthResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        // Verificar senha manualmente
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciais inválidas");
        }
        
        // Atualizar último login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        
        String jwtToken = jwtService.generateToken(user);
        
        return new AuthResponseDTO(
                jwtToken,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRole()
        );
    }
    
    public AuthResponseDTO register(RegisterRequestDTO request) {
        // Verificar se username já existe
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username já está em uso");
        }
        
        // Verificar se email já existe
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }
        
        // Criar novo usuário
        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getFullName(),
                request.getRole()
        );
        
        User savedUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(savedUser);
        
        return new AuthResponseDTO(
                jwtToken,
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getRole()
        );
    }
    
    public User createSuperUser(String username, String email, String password, String fullName) {
        // Verificar se já existe um admin
        List<User> admins = userRepository.findByRole(Role.ADMIN);
        if (!admins.isEmpty()) {
            throw new RuntimeException("Já existe um superusuário no sistema");
        }
        
        // Verificar se username já existe
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username já está em uso");
        }
        
        // Verificar se email já existe
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email já está em uso");
        }
        
        User superUser = new User(
                username,
                email,
                passwordEncoder.encode(password),
                fullName,
                Role.ADMIN
        );
        
        return userRepository.save(superUser);
    }
    
    public List<String> getTechnicianNames() {
        return userRepository.findTechnicianNames();
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public boolean hasUsers() {
        return userRepository.count() > 0;
    }
    
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    public boolean hasAdminUser() {
        return !userRepository.findByRole(Role.ADMIN).isEmpty();
    }
}