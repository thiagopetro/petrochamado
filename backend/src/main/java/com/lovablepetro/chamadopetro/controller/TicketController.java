package com.lovablepetro.chamadopetro.controller;

import com.lovablepetro.chamadopetro.dto.DashboardMetricsDTO;
import com.lovablepetro.chamadopetro.dto.TicketDTO;
import com.lovablepetro.chamadopetro.dto.ImportResultDTO;
import com.lovablepetro.chamadopetro.service.TicketService;
import com.lovablepetro.chamadopetro.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    
    @Autowired
    private TicketService ticketService;
    
    @Autowired
    private AuthService authService;
    
    @GetMapping
    public ResponseEntity<List<TicketDTO>> getAllTickets(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String prioridade,
            @RequestParam(required = false) String atribuidoA) {
        
        List<TicketDTO> tickets;
        
        if (search != null || status != null || prioridade != null || atribuidoA != null) {
            tickets = ticketService.searchTickets(search, status, prioridade, atribuidoA);
        } else {
            tickets = ticketService.getAllTickets();
        }
        
        return ResponseEntity.ok(tickets);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long id) {
        Optional<TicketDTO> ticket = ticketService.getTicketById(id);
        return ticket.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<TicketDTO> getTicketByTicketId(@PathVariable String ticketId) {
        Optional<TicketDTO> ticket = ticketService.getTicketByTicketId(ticketId);
        return ticket.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@Valid @RequestBody TicketDTO ticketDTO) {
        try {
            TicketDTO createdTicket = ticketService.createTicket(ticketDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long id, 
                                                 @Valid @RequestBody TicketDTO ticketDTO) {
        try {
            Optional<TicketDTO> updatedTicket = ticketService.updateTicket(id, ticketDTO);
            return updatedTicket.map(ResponseEntity::ok)
                               .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        boolean deleted = ticketService.deleteTicket(id);
        return deleted ? ResponseEntity.noContent().build() 
                      : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/dashboard/metrics")
    public ResponseEntity<DashboardMetricsDTO> getDashboardMetrics() {
        DashboardMetricsDTO metrics = ticketService.getDashboardMetrics();
        return ResponseEntity.ok(metrics);
    }
    
    @GetMapping("/technicians")
    public ResponseEntity<List<String>> getTechnicians() {
        List<String> technicians = authService.getTechnicianNames();
        return ResponseEntity.ok(technicians);
    }
    
    @GetMapping("/priorities")
    public ResponseEntity<List<String>> getPriorities() {
        List<String> priorities = List.of(
            "1 - Crítica",
            "2 - Alta",
            "3 - Moderada",
            "4 - Baixa"
        );
        return ResponseEntity.ok(priorities);
    }
    
    @GetMapping("/statuses")
    public ResponseEntity<List<String>> getStatuses() {
        List<String> statuses = List.of(
            "Aguardando usuário",
            "Em atendimento",
            "Problema confirmado",
            "Resolvido"
        );
        return ResponseEntity.ok(statuses);
    }
    
    @PostMapping("/import")
    public ResponseEntity<ImportResultDTO> importTickets(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                ImportResultDTO result = new ImportResultDTO();
                result.addError("Arquivo não pode estar vazio");
                return ResponseEntity.badRequest().body(result);
            }
            
            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".csv") && !fileName.endsWith(".xlsx") && !fileName.endsWith(".txt"))) {
                ImportResultDTO result = new ImportResultDTO();
                result.addError("Formato de arquivo não suportado. Use CSV, XLSX ou TXT.");
                return ResponseEntity.badRequest().body(result);
            }
            
            ImportResultDTO result = ticketService.importTicketsFromFile(file);
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            ImportResultDTO result = new ImportResultDTO();
            result.addError("Erro interno do servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}