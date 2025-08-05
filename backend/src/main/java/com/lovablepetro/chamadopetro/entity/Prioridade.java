package com.lovablepetro.chamadopetro.entity;

public enum Prioridade {
    CRITICA("1 - Crítica"),
    ALTA("2 - Alta"),
    MODERADA("3 - Moderada"),
    BAIXA("4 - Baixa");
    
    private final String descricao;
    
    Prioridade(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public static Prioridade fromDescricao(String descricao) {
        for (Prioridade prioridade : values()) {
            if (prioridade.getDescricao().equals(descricao)) {
                return prioridade;
            }
        }
        throw new IllegalArgumentException("Prioridade não encontrada: " + descricao);
    }
}