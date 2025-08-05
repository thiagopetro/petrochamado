package com.lovablepetro.chamadopetro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class TicketDTO {
    
    private Long id;
    private String ticketId;
    
    @NotBlank(message = "Título é obrigatório")
    private String titulo;
    
    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;
    
    @NotNull(message = "Prioridade é obrigatória")
    private String prioridade;
    
    private LocalDateTime abertoEm;
    
    @NotBlank(message = "Aberto por é obrigatório")
    private String abertoPor;
    
    private String emailAbertoPor;
    
    @NotBlank(message = "Atribuído a é obrigatório")
    private String atribuidoA;
    
    private LocalDateTime atualizado;
    
    @NotNull(message = "Status é obrigatório")
    private String status;
    
    // Construtores
    public TicketDTO() {}
    
    public TicketDTO(String titulo, String descricao, String prioridade, 
                     String abertoPor, String atribuidoA, String status) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.abertoPor = abertoPor;
        this.atribuidoA = atribuidoA;
        this.status = status;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTicketId() {
        return ticketId;
    }
    
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getPrioridade() {
        return prioridade;
    }
    
    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }
    
    public LocalDateTime getAbertoEm() {
        return abertoEm;
    }
    
    public void setAbertoEm(LocalDateTime abertoEm) {
        this.abertoEm = abertoEm;
    }
    
    public String getAbertoPor() {
        return abertoPor;
    }
    
    public void setAbertoPor(String abertoPor) {
        this.abertoPor = abertoPor;
    }
    
    public String getEmailAbertoPor() {
        return emailAbertoPor;
    }
    
    public void setEmailAbertoPor(String emailAbertoPor) {
        this.emailAbertoPor = emailAbertoPor;
    }
    
    public String getAtribuidoA() {
        return atribuidoA;
    }
    
    public void setAtribuidoA(String atribuidoA) {
        this.atribuidoA = atribuidoA;
    }
    
    public LocalDateTime getAtualizado() {
        return atualizado;
    }
    
    public void setAtualizado(LocalDateTime atualizado) {
        this.atualizado = atualizado;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}