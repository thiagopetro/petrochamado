package com.lovablepetro.chamadopetro.dto;

import java.util.List;


public class DashboardMetricsDTO {
    
    private long totalTickets;
    private long openTickets;
    private long resolvedTickets;
    private long pendingTickets;
    private List<TechnicianStatsDTO> technicianStats;
    private List<PriorityStatsDTO> priorityStats;
    private List<StatusStatsDTO> statusStats;
    
    // Construtores
    public DashboardMetricsDTO() {}
    
    public DashboardMetricsDTO(long totalTickets, long openTickets, 
                              long resolvedTickets, long pendingTickets) {
        this.totalTickets = totalTickets;
        this.openTickets = openTickets;
        this.resolvedTickets = resolvedTickets;
        this.pendingTickets = pendingTickets;
    }
    
    // Getters e Setters
    public long getTotalTickets() {
        return totalTickets;
    }
    
    public void setTotalTickets(long totalTickets) {
        this.totalTickets = totalTickets;
    }
    
    public long getOpenTickets() {
        return openTickets;
    }
    
    public void setOpenTickets(long openTickets) {
        this.openTickets = openTickets;
    }
    
    public long getResolvedTickets() {
        return resolvedTickets;
    }
    
    public void setResolvedTickets(long resolvedTickets) {
        this.resolvedTickets = resolvedTickets;
    }
    
    public long getPendingTickets() {
        return pendingTickets;
    }
    
    public void setPendingTickets(long pendingTickets) {
        this.pendingTickets = pendingTickets;
    }
    
    public List<TechnicianStatsDTO> getTechnicianStats() {
        return technicianStats;
    }
    
    public void setTechnicianStats(List<TechnicianStatsDTO> technicianStats) {
        this.technicianStats = technicianStats;
    }
    
    public List<PriorityStatsDTO> getPriorityStats() {
        return priorityStats;
    }
    
    public void setPriorityStats(List<PriorityStatsDTO> priorityStats) {
        this.priorityStats = priorityStats;
    }
    
    public List<StatusStatsDTO> getStatusStats() {
        return statusStats;
    }
    
    public void setStatusStats(List<StatusStatsDTO> statusStats) {
        this.statusStats = statusStats;
    }
    
    // Classes internas para estatísticas
    public static class TechnicianStatsDTO {
        private String name;
        private long tickets;
        
        public TechnicianStatsDTO() {}
        
        public TechnicianStatsDTO(String name, long tickets) {
            this.name = name;
            this.tickets = tickets;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public long getTickets() {
            return tickets;
        }
        
        public void setTickets(long tickets) {
            this.tickets = tickets;
        }
    }
    
    public static class PriorityStatsDTO {
        private String name;
        private long value;
        private String color;
        
        public PriorityStatsDTO() {}
        
        public PriorityStatsDTO(String name, long value, String color) {
            this.name = name;
            this.value = value;
            this.color = color;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public long getValue() {
            return value;
        }
        
        public void setValue(long value) {
            this.value = value;
        }
        
        public String getColor() {
            return color;
        }
        
        public void setColor(String color) {
            this.color = color;
        }
    }
    
    public static class StatusStatsDTO {
        private String name;
        private long value;
        
        public StatusStatsDTO() {}
        
        public StatusStatsDTO(String name, long value) {
            this.name = name;
            this.value = value;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public long getValue() {
            return value;
        }
        
        public void setValue(long value) {
            this.value = value;
        }
    }
}