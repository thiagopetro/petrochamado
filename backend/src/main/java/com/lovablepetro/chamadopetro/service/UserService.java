package com.lovablepetro.chamadopetro.service;

import com.lovablepetro.chamadopetro.controller.UserController.UserStatsDTO;
import com.lovablepetro.chamadopetro.dto.UserDTO;
import com.lovablepetro.chamadopetro.dto.UserSafeDTO;
import com.lovablepetro.chamadopetro.entity.Role;
import com.lovablepetro.chamadopetro.entity.User;
import com.lovablepetro.chamadopetro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Obter todos os usuários com paginação
     */
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(this::convertToDTO);
    }
    
    /**
     * Obter todos os usuários com paginação (versão segura sem senha)
     */
    public Page<UserSafeDTO> getAllUsersSafe(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(UserSafeDTO::new);
    }

    /**
     * Buscar usuários por nome ou login
     */
    public List<UserDTO> searchUsers(String query) {
        List<User> users = userRepository.findByFullNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(query, query);
        return users.stream()
                   .map(this::convertToDTO)
                   .collect(Collectors.toList());
    }
    
    /**
     * Buscar usuários por nome ou login (versão segura sem senha)
     */
    public List<UserSafeDTO> searchUsersSafe(String query) {
        List<User> users = userRepository.findByFullNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(query, query);
        return users.stream()
                   .map(UserSafeDTO::new)
                   .collect(Collectors.toList());
    }

    /**
     * Obter usuário por ID
     */
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id)
                           .map(this::convertToDTO);
    }
    
    /**
     * Obter usuário por ID (versão segura sem senha)
     */
    public Optional<UserSafeDTO> getUserByIdSafe(Long id) {
        return userRepository.findById(id)
                           .map(UserSafeDTO::new);
    }

    /**
     * Criar novo usuário
     */
    public UserDTO createUser(UserDTO userDTO) {
        // Verificar se login já existe
        if (userRepository.existsByUsername(userDTO.getLogin())) {
            throw new IllegalArgumentException("Login já existe");
        }

        User user = new User();
        user.setFullName(userDTO.getNome());
        user.setUsername(userDTO.getLogin());
        user.setEmail(userDTO.getLogin() + "@lovablepetro.com"); // Email padrão baseado no login
        user.setPassword(passwordEncoder.encode(userDTO.getSenha()));
        user.setRole(Role.USER);
        user.setIsActive(userDTO.getAtivo() != null ? userDTO.getAtivo() : true);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    /**
     * Atualizar usuário existente
     */
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                                 .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // Verificar se o novo login já existe (exceto para o próprio usuário)
        if (!user.getUsername().equals(userDTO.getLogin()) && 
            userRepository.existsByUsername(userDTO.getLogin())) {
            throw new IllegalArgumentException("Login já existe");
        }

        user.setFullName(userDTO.getNome());
        user.setUsername(userDTO.getLogin());
        user.setEmail(userDTO.getLogin() + "@lovablepetro.com");
        
        // Só atualizar senha se foi fornecida
        if (userDTO.getSenha() != null && !userDTO.getSenha().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getSenha()));
        }
        
        user.setIsActive(userDTO.getAtivo());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    /**
     * Ativar/Desativar usuário
     */
    public UserDTO toggleUserStatus(Long id) {
        User user = userRepository.findById(id)
                                 .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        user.setIsActive(!user.getIsActive());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    /**
     * Deletar usuário
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        userRepository.deleteById(id);
    }

    /**
     * Verificar se login já existe
     */
    public boolean loginExists(String login) {
        return userRepository.existsByUsername(login);
    }

    /**
     * Obter estatísticas de usuários
     */
    public UserStatsDTO getUserStats() {
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countByIsActive(true);
        long inactiveUsers = userRepository.countByIsActive(false);
        
        return new UserStatsDTO(totalUsers, activeUsers, inactiveUsers);
    }

    /**
     * Converter entidade User para DTO
     */
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setNome(user.getFullName());
        dto.setLogin(user.getUsername());
        // Não retornar senha por segurança
        dto.setSenha(null);
        dto.setAtivo(user.getIsActive());
        dto.setCriadoEm(user.getCreatedAt());
        dto.setAtualizadoEm(user.getUpdatedAt());
        return dto;
    }
}