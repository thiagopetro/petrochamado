package com.lovablepetro.chamadopetro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String ticketId;
    
    @NotBlank(message = "Título é obrigatório")
    @Column(nullable = false)
    private String titulo;
    
    @NotBlank(message = "Descrição é obrigatória")
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    @NotNull(message = "Prioridade é obrigatória")
    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;
    
    @Column(nullable = false)
    private LocalDateTime abertoEm;
    
    @NotBlank(message = "Aberto por é obrigatório")
    @Column(nullable = false)
    private String abertoPor;
    
    @Column(nullable = true)
    private String emailAbertoPor;
    
    @NotBlank(message = "Atribuído a é obrigatório")
    @Column(nullable = false)
    private String atribuidoA;
    
    @Column(nullable = false)
    private LocalDateTime atualizado;
    
    @NotNull(message = "Status é obrigatório")
    @Enumerated(EnumType.STRING)
    private Status status;
    
    // Construtores
    public Ticket() {
        this.abertoEm = LocalDateTime.now();
        this.atualizado = LocalDateTime.now();
    }
    
    public Ticket(String titulo, String descricao, Prioridade prioridade, 
                  String abertoPor, String atribuidoA, Status status) {
        this();
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
    
    public Prioridade getPrioridade() {
        return prioridade;
    }
    
    public void setPrioridade(Prioridade prioridade) {
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
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.atualizado = LocalDateTime.now();
    }
    
    @PrePersist
    public void prePersist() {
        if (this.ticketId == null) {
            this.ticketId = generateTicketId();
        }
    }
    
    private String generateTicketId() {
        // Gera um número sequencial de 7 dígitos baseado no timestamp
        long timestamp = System.currentTimeMillis();
        // Usa os últimos 7 dígitos do timestamp para criar um número como 3221310
        long ticketNumber = timestamp % 10000000L;
        // Garante que sempre tenha 7 dígitos, preenchendo com zeros à esquerda se necessário
        return String.format("INC%07d", ticketNumber);
    }
}