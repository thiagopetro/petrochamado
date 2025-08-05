package com.lovablepetro.chamadopetro.config;

import com.lovablepetro.chamadopetro.entity.Prioridade;
import com.lovablepetro.chamadopetro.entity.Role;
import com.lovablepetro.chamadopetro.entity.Status;
import com.lovablepetro.chamadopetro.entity.Ticket;
import com.lovablepetro.chamadopetro.entity.User;
import com.lovablepetro.chamadopetro.repository.TicketRepository;
import com.lovablepetro.chamadopetro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Criar usuário admin padrão se não existir
        if (userRepository.count() == 0) {
            initializeDefaultAdmin();
        }
        
        // Criar tickets de exemplo se não existirem
        // if (ticketRepository.count() == 0) {
        //     initializeTickets();
        // }
    }
    
    private void initializeDefaultAdmin() {
        User admin = new User(
                "admin",
                "admin@petro.com",
                passwordEncoder.encode("admin123"),
                "Administrador do Sistema",
                Role.ADMIN
        );
        
        userRepository.save(admin);
        System.out.println("Usuário admin padrão criado:");
        System.out.println("Username: admin");
        System.out.println("Password: admin123");
        System.out.println("Email: admin@petro.com");
    }
    

}