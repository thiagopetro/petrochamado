package com.lovablepetro.chamadopetro.repository;

import com.lovablepetro.chamadopetro.entity.Ticket;
import com.lovablepetro.chamadopetro.entity.Status;
import com.lovablepetro.chamadopetro.entity.Prioridade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
    Optional<Ticket> findByTicketId(String ticketId);
    
    List<Ticket> findByStatus(Status status);
    
    List<Ticket> findByPrioridade(Prioridade prioridade);
    
    List<Ticket> findByAtribuidoA(String atribuidoA);
    
    List<Ticket> findByAbertoPor(String abertoPor);
    
    @Query("SELECT t FROM Ticket t WHERE " +
           "LOWER(t.titulo) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(t.ticketId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(t.abertoPor) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Ticket> findBySearchTerm(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT t FROM Ticket t WHERE " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:prioridade IS NULL OR t.prioridade = :prioridade) AND " +
           "(:atribuidoA IS NULL OR t.atribuidoA = :atribuidoA) AND " +
           "(:searchTerm IS NULL OR " +
           "LOWER(t.titulo) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(t.ticketId) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(t.abertoPor) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Ticket> findByFilters(@Param("status") Status status,
                              @Param("prioridade") Prioridade prioridade,
                              @Param("atribuidoA") String atribuidoA,
                              @Param("searchTerm") String searchTerm);
    
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status != 'RESOLVIDO'")
    long countOpenTickets();
    
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status = 'RESOLVIDO'")
    long countResolvedTickets();
    
    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.status = 'AGUARDANDO_USUARIO'")
    long countPendingTickets();
    
    @Query("SELECT t.atribuidoA, COUNT(t) FROM Ticket t GROUP BY t.atribuidoA")
    List<Object[]> countTicketsByTechnician();
    
    @Query("SELECT t.prioridade, COUNT(t) FROM Ticket t GROUP BY t.prioridade")
    List<Object[]> countTicketsByPriority();
    
    @Query("SELECT t.status, COUNT(t) FROM Ticket t GROUP BY t.status")
    List<Object[]> countTicketsByStatus();
}