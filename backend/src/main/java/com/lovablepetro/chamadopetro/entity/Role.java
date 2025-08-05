package com.lovablepetro.chamadopetro.entity;

public enum Role {
    ADMIN("Administrador"),
    USER("Usuário"),
    TECHNICIAN("Técnico");
    
    private final String descricao;
    
    Role(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public static Role fromDescricao(String descricao) {
        for (Role role : Role.values()) {
            if (role.getDescricao().equals(descricao)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Role não encontrado: " + descricao);
    }
}