package com.lovablepetro.chamadopetro.entity;

public enum Status {
    AGUARDANDO_USUARIO("Aguardando usuário"),
    EM_ATENDIMENTO("Em atendimento"),
    PROBLEMA_CONFIRMADO("Problema confirmado"),
    RESOLVIDO("Resolvido");
    
    private final String descricao;
    
    Status(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public static Status fromDescricao(String descricao) {
        for (Status status : values()) {
            if (status.getDescricao().equals(descricao)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status não encontrado: " + descricao);
    }
}