package com.lovablepetro.chamadopetro.dto;

import com.lovablepetro.chamadopetro.entity.Role;
import com.lovablepetro.chamadopetro.entity.User;

import java.time.LocalDateTime;

/**
 * DTO seguro para retornar dados de usuário sem a senha
 */
public class UserSafeDTO {
    
    private Long id;
    private String nome;
    private String login;
    private String email;
    private Role role;
    private Boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
    private LocalDateTime ultimoLogin;
    
    // Constructors
    public UserSafeDTO() {}
    
    public UserSafeDTO(User user) {
        this.id = user.getId();
        this.nome = user.getFullName();
        this.login = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.ativo = user.getIsActive();
        this.criadoEm = user.getCreatedAt();
        this.atualizadoEm = user.getUpdatedAt();
        this.ultimoLogin = user.getLastLogin();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getLogin() {
        return login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public Boolean getAtivo() {
        return ativo;
    }
    
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    
    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
    
    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }
    
    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
    
    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }
    
    public LocalDateTime getUltimoLogin() {
        return ultimoLogin;
    }
    
    public void setUltimoLogin(LocalDateTime ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }
}