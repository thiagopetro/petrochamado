package com.lovablepetro.chamadopetro.service;

import com.lovablepetro.chamadopetro.dto.DashboardMetricsDTO;
import com.lovablepetro.chamadopetro.dto.TicketDTO;
import com.lovablepetro.chamadopetro.dto.ImportResultDTO;
import com.lovablepetro.chamadopetro.entity.Prioridade;
import com.lovablepetro.chamadopetro.entity.Status;
import com.lovablepetro.chamadopetro.entity.Ticket;
import com.lovablepetro.chamadopetro.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@Service
public class TicketService {
    
    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);
    
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
        try {
            Ticket ticket = convertToEntity(ticketDTO);
            Ticket savedTicket = ticketRepository.save(ticket);
            return convertToDTO(savedTicket);
        } catch (Exception e) {
            logger.error("Erro ao criar ticket: {}", e.getMessage(), e);
            throw e;
        }
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
    
    public ImportResultDTO importTicketsFromFile(MultipartFile file) {
        ImportResultDTO result = new ImportResultDTO();
        
        System.out.println("=== INICIANDO IMPORTAÇÃO DE TICKETS ===");
        System.out.println("Nome do arquivo: " + file.getOriginalFilename());
        System.out.println("Tamanho do arquivo: " + file.getSize() + " bytes");
        
        // Tentar diferentes codificações
        String[] encodings = {"UTF-8", "ISO-8859-1", "Windows-1252", "UTF-16"};
        
        for (String encoding : encodings) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(file.getInputStream(), encoding))) {
                
                System.out.println("Tentando codificação: " + encoding);
                
                String line;
                int lineNumber = 0;
                boolean isFirstLine = true;
                boolean foundValidData = false;
                
                while ((line = reader.readLine()) != null) {
                    lineNumber++;
                    
                    System.out.println("Linha " + lineNumber + " (" + encoding + "): " + line);
                    
                    // Pular a primeira linha se for cabeçalho
                    if (isFirstLine) {
                        isFirstLine = false;
                        // Verificar se a primeira linha parece ser um cabeçalho
                        if (line.toLowerCase().contains("número") || 
                            line.toLowerCase().contains("numero") ||
                            line.toLowerCase().contains("prioridade") ||
                            line.toLowerCase().contains("aberto") ||
                            line.toLowerCase().contains("ticketid") || 
                            line.toLowerCase().contains("titulo") ||
                            line.toLowerCase().contains("código") ||
                            line.toLowerCase().contains("codigo") ||
                            line.contains("N�mero")) { // Detectar cabeçalho com caracteres corrompidos

                            System.out.println("Detectado cabeçalho, pulando linha 1");
                            continue;
                        }
                    }
                    
                    if (line.trim().isEmpty()) {
                        System.out.println("Linha vazia, pulando");
                        continue;
                    }
                    
                    try {
                        TicketDTO ticketDTO = parseLineToTicketDTO(line, lineNumber);
                        System.out.println("TicketDTO criado: ID=" + ticketDTO.getTicketId() + ", Título=" + ticketDTO.getTitulo());
                        
                        // Verificar se já existe um ticket com o mesmo ticketId
                        if (ticketRepository.findByTicketId(ticketDTO.getTicketId()).isPresent()) {
                            System.out.println("Ticket duplicado encontrado: " + ticketDTO.getTicketId());
                            result.addDuplicate(ticketDTO.getTicketId());
                            continue;
                        }
                        
                        // Criar o ticket
                        Ticket ticket = convertToEntity(ticketDTO);
                        ticketRepository.save(ticket);
                        result.incrementSuccess();
                        foundValidData = true;
                        System.out.println("Ticket salvo com sucesso: " + ticketDTO.getTicketId());
                        
                    } catch (Exception e) {
                        System.out.println("Erro ao processar linha " + lineNumber + ": " + e.getMessage());
                        result.addError("Linha " + lineNumber + ": " + e.getMessage());
                    }
                }
                
                // Se encontrou dados válidos com esta codificação, parar de tentar outras
                if (foundValidData || result.getSuccess() > 0) {
                    System.out.println("Codificação " + encoding + " funcionou! Dados processados com sucesso.");
                    break;
                }
                
            } catch (Exception e) {
                System.out.println("Erro com codificação " + encoding + ": " + e.getMessage());
                if (encoding.equals(encodings[encodings.length - 1])) {
                    result.addError("Erro ao processar arquivo com todas as codificações testadas: " + e.getMessage());
                }
            }
        }
        
        System.out.println("=== RESULTADO DA IMPORTAÇÃO ===");
        System.out.println("Sucessos: " + result.getSuccess());
        System.out.println("Erros: " + result.getErrors().size());
        System.out.println("Duplicados: " + result.getDuplicates().size());
        
        return result;
    }
    
    private TicketDTO parseLineToTicketDTO(String line, int lineNumber) throws Exception {
        // Dividir a linha por vírgulas, considerando aspas
        String[] fields = parseCSVLine(line);
        

        if (fields.length < 6) {
            throw new Exception("Linha " + lineNumber + ": Linha deve conter pelo menos 6 campos: Número, Prioridade, Aberto(a) por, Aberto(a), Atribuído(a) a, Descrição resumida. Encontrados: " + fields.length + " campos.");
        }
        
        System.out.println("Linha " + lineNumber + " tem " + fields.length + " campos:");
        for (int i = 0; i < fields.length; i++) {
            System.out.println("  Campo " + i + ": '" + fields[i] + "'");
        }
        
        TicketDTO dto = new TicketDTO();
        
        try {
            // Mapeamento conforme estrutura real do CSV:
            // Campo 0: Número = ticketId
            // Campo 1: Prioridade = prioridade 
            // Campo 2: Aberto(a) por = abertoPor
            // Campo 3: Aberto(a) = data de abertura - será ignorada
            // Campo 4: Atribuído(a) a = atribuidoA - opcional
            // Campo 5: Descrição resumida = descricao
            // Campos 6 e 7: vazios (ignorados)
            
            dto.setTicketId(fields[0].trim());
            dto.setPrioridade(normalizePriority(fields[1].trim()));
            dto.setAbertoPor(fields[2].trim());
            // fields[3] é a data de abertura - será definida automaticamente como data atual
            
            // Campo Atribuído - opcional, não obrigatório
            String atribuidoValue = fields[4].trim();
            dto.setAtribuidoA(!atribuidoValue.isEmpty() ? atribuidoValue : "Não atribuído");
            
            // Descrição resumida
            dto.setDescricao(fields[5].trim());
            
            // Título baseado na descrição (opcional, não obrigatório)
            String descricao = dto.getDescricao();
            String titulo = descricao.length() > 50 ? descricao.substring(0, 50) + "..." : descricao;
            dto.setTitulo(titulo);
            
            // Status padrão para novos chamados (opcional, não obrigatório)
            dto.setStatus(normalizeStatus("Aberto"));
            
            // Validações apenas para campos obrigatórios
            if (dto.getTicketId().isEmpty()) {
                throw new Exception("Número do chamado não pode estar vazio");
            }
            if (dto.getDescricao().isEmpty()) {
                throw new Exception("Descrição resumida não pode estar vazia");
            }
            if (dto.getAbertoPor().isEmpty()) {
                throw new Exception("Campo 'Aberto(a) por' não pode estar vazio");
            }
            
        } catch (Exception e) {
            throw new Exception("Erro ao processar campos: " + e.getMessage());
        }
        
        return dto;
    }
    
    private String[] parseCSVLine(String line) {
        // Detectar o separador automaticamente
        char separator = detectSeparator(line);
        
        List<String> fields = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == separator && !inQuotes) {
                fields.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }
        
        fields.add(currentField.toString());
        return fields.toArray(new String[0]);
    }
    
    private char detectSeparator(String line) {
        // Contar vírgulas e ponto e vírgulas fora de aspas
        int commaCount = 0;
        int semicolonCount = 0;
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (!inQuotes) {
                if (c == ',') {
                    commaCount++;
                } else if (c == ';') {
                    semicolonCount++;
                }
            }
        }
        
        // Retornar o separador mais comum
        return semicolonCount > commaCount ? ';' : ',';
    }
    
    private String normalizePriority(String priority) {
        String normalized = priority.toLowerCase().trim();
        switch (normalized) {
            case "crítica":
            case "critica":
            case "critical":
            case "1":
            case "1 - crítica":
            case "1 - critica":
                return "1 - Crítica";
            case "alta":
            case "high":
            case "2":
            case "2 - alta":
                return "2 - Alta";
            case "média":
            case "media":
            case "moderada":
            case "medium":
            case "3":
            case "3 - moderada":
            case "3 - média":
            case "3 - media":
                return "3 - Moderada";
            case "baixa":
            case "low":
            case "4":
            case "4 - baixa":
                return "4 - Baixa";
            default:
                return "3 - Moderada"; // Default
        }
    }
    
    private String normalizeStatus(String status) {
        String normalized = status.toLowerCase().trim();
        switch (normalized) {
            case "aberto":
            case "open":
            case "novo":
            case "new":
            case "aguardando usuário":
            case "aguardando_usuario":
            case "waiting":
            case "pending":
                return "Aguardando usuário";
            case "em atendimento":
            case "em_atendimento":
            case "in progress":
            case "progress":
                return "Em atendimento";
            case "problema confirmado":
            case "problema_confirmado":
            case "confirmed":
                return "Problema confirmado";
            case "resolvido":
            case "resolved":
            case "closed":
            case "fechado":
                return "Resolvido";
            default:
                return "Aberto"; // Default
        }
    }
}