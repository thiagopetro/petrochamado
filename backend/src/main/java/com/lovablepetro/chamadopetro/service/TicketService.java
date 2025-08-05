package com.lovablepetro.chamadopetro.service;

import com.lovablepetro.chamadopetro.dto.DashboardMetricsDTO;
import com.lovablepetro.chamadopetro.dto.TicketDTO;
import com.lovablepetro.chamadopetro.entity.Prioridade;
import com.lovablepetro.chamadopetro.entity.Status;
import com.lovablepetro.chamadopetro.entity.Ticket;
import com.lovablepetro.chamadopetro.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

@Service
public class TicketService {
    
    @Autowired
    private TicketRepository ticketRepository;
    
    public List<TicketDTO> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<TicketDTO> getTicketById(Long id) {
        return ticketRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public Optional<TicketDTO> getTicketByTicketId(String ticketId) {
        return ticketRepository.findByTicketId(ticketId)
                .map(this::convertToDTO);
    }
    
    public TicketDTO createTicket(TicketDTO ticketDTO) {
        Ticket ticket = convertToEntity(ticketDTO);
        Ticket savedTicket = ticketRepository.save(ticket);
        return convertToDTO(savedTicket);
    }
    
    public Optional<TicketDTO> updateTicket(Long id, TicketDTO ticketDTO) {
        return ticketRepository.findById(id)
                .map(existingTicket -> {
                    updateTicketFromDTO(existingTicket, ticketDTO);
                    Ticket savedTicket = ticketRepository.save(existingTicket);
                    return convertToDTO(savedTicket);
                });
    }
    
    public boolean deleteTicket(Long id) {
        if (ticketRepository.existsById(id)) {
            ticketRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public List<TicketDTO> searchTickets(String searchTerm, String status, 
                                        String prioridade, String atribuidoA) {
        Status statusEnum = null;
        if (status != null && !status.equals("all")) {
            statusEnum = Status.fromDescricao(status);
        }
        
        Prioridade prioridadeEnum = null;
        if (prioridade != null && !prioridade.equals("all")) {
            prioridadeEnum = Prioridade.fromDescricao(prioridade);
        }
        
        String atribuidoAFilter = null;
        if (atribuidoA != null && !atribuidoA.equals("all")) {
            atribuidoAFilter = atribuidoA;
        }
        
        String searchFilter = null;
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            searchFilter = searchTerm.trim();
        }
        
        return ticketRepository.findByFilters(statusEnum, prioridadeEnum, 
                                             atribuidoAFilter, searchFilter)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public DashboardMetricsDTO getDashboardMetrics() {
        long totalTickets = ticketRepository.count();
        long openTickets = ticketRepository.countOpenTickets();
        long resolvedTickets = ticketRepository.countResolvedTickets();
        long pendingTickets = ticketRepository.countPendingTickets();
        
        DashboardMetricsDTO metrics = new DashboardMetricsDTO(
            totalTickets, openTickets, resolvedTickets, pendingTickets
        );
        
        // Estatísticas por técnico
        List<Object[]> technicianData = ticketRepository.countTicketsByTechnician();
        List<DashboardMetricsDTO.TechnicianStatsDTO> technicianStats = technicianData.stream()
                .map(data -> new DashboardMetricsDTO.TechnicianStatsDTO(
                    ((String) data[0]).split(" ")[0], // Primeiro nome
                    (Long) data[1]
                ))
                .collect(Collectors.toList());
        metrics.setTechnicianStats(technicianStats);
        
        // Estatísticas por prioridade
        List<Object[]> priorityData = ticketRepository.countTicketsByPriority();
        Map<String, String> priorityColors = getPriorityColors();
        List<DashboardMetricsDTO.PriorityStatsDTO> priorityStats = priorityData.stream()
                .map(data -> {
                    Prioridade prioridade = (Prioridade) data[0];
                    String name = prioridade.getDescricao().split(" - ")[1]; // Remove número
                    return new DashboardMetricsDTO.PriorityStatsDTO(
                        name,
                        (Long) data[1],
                        priorityColors.get(name)
                    );
                })
                .collect(Collectors.toList());
        metrics.setPriorityStats(priorityStats);
        
        // Estatísticas por status
        List<Object[]> statusData = ticketRepository.countTicketsByStatus();
        List<DashboardMetricsDTO.StatusStatsDTO> statusStats = statusData.stream()
                .map(data -> new DashboardMetricsDTO.StatusStatsDTO(
                    ((Status) data[0]).getDescricao(),
                    (Long) data[1]
                ))
                .collect(Collectors.toList());
        metrics.setStatusStats(statusStats);
        
        return metrics;
    }
    
    private TicketDTO convertToDTO(Ticket ticket) {
        TicketDTO dto = new TicketDTO();
        dto.setId(ticket.getId());
        dto.setTicketId(ticket.getTicketId());
        dto.setTitulo(ticket.getTitulo());
        dto.setDescricao(ticket.getDescricao());
        dto.setPrioridade(ticket.getPrioridade().getDescricao());
        dto.setAbertoEm(ticket.getAbertoEm());
        dto.setAbertoPor(ticket.getAbertoPor());
        dto.setEmailAbertoPor(ticket.getEmailAbertoPor());
        dto.setAtribuidoA(ticket.getAtribuidoA());
        dto.setAtualizado(ticket.getAtualizado());
        dto.setStatus(ticket.getStatus().getDescricao());
        return dto;
    }
    
    private Ticket convertToEntity(TicketDTO dto) {
        Ticket ticket = new Ticket();
        if (dto.getTicketId() != null && !dto.getTicketId().trim().isEmpty()) {
            ticket.setTicketId(dto.getTicketId());
        }
        ticket.setTitulo(dto.getTitulo());
        ticket.setDescricao(dto.getDescricao());
        ticket.setPrioridade(Prioridade.fromDescricao(dto.getPrioridade()));
        ticket.setAbertoPor(dto.getAbertoPor());
        ticket.setEmailAbertoPor(dto.getEmailAbertoPor());
        ticket.setAtribuidoA(dto.getAtribuidoA());
        ticket.setStatus(Status.fromDescricao(dto.getStatus()));
        return ticket;
    }
    
    private void updateTicketFromDTO(Ticket ticket, TicketDTO dto) {
        ticket.setTitulo(dto.getTitulo());
        ticket.setDescricao(dto.getDescricao());
        ticket.setPrioridade(Prioridade.fromDescricao(dto.getPrioridade()));
        ticket.setAbertoPor(dto.getAbertoPor());
        ticket.setEmailAbertoPor(dto.getEmailAbertoPor());
        ticket.setAtribuidoA(dto.getAtribuidoA());
        ticket.setStatus(Status.fromDescricao(dto.getStatus()));
    }
    
    private Map<String, String> getPriorityColors() {
        Map<String, String> colors = new HashMap<>();
        colors.put("Crítica", "#ef4444");
        colors.put("Alta", "#f97316");
        colors.put("Moderada", "#eab308");
        colors.put("Baixa", "#22c55e");
        return colors;
    }
}