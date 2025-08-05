package com.lovablepetro.chamadopetro.config;

import com.lovablepetro.chamadopetro.entity.Role;
import com.lovablepetro.chamadopetro.entity.User;
import com.lovablepetro.chamadopetro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserDataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verificar se já existem usuários no banco
        if (userRepository.count() == 0) {
            createInitialUsers();
        }
    }

    private void createInitialUsers() {
        // Usuário administrador
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@lovablepetro.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFullName("Administrador do Sistema");
        admin.setRole(Role.ADMIN);
        admin.setIsActive(true);
        admin.setCreatedAt(LocalDateTime.now());
        userRepository.save(admin);

        // Usuários de exemplo
        createSampleUser("joao.silva", "João Silva", "123456", true);
        createSampleUser("maria.santos", "Maria Santos", "123456", true);
        createSampleUser("pedro.oliveira", "Pedro Oliveira", "123456", false);
        createSampleUser("ana.costa", "Ana Costa", "123456", true);
        createSampleUser("carlos.ferreira", "Carlos Ferreira", "123456", true);
        createSampleUser("lucia.almeida", "Lúcia Almeida", "123456", false);

        System.out.println("Usuários de exemplo criados com sucesso!");
        System.out.println("Login do admin: admin / Senha: admin123");
        System.out.println("Login dos usuários: [nome.sobrenome] / Senha: 123456");
    }

    private void createSampleUser(String login, String nome, String senha, boolean ativo) {
        User user = new User();
        user.setUsername(login);
        user.setEmail(login + "@lovablepetro.com");
        user.setPassword(passwordEncoder.encode(senha));
        user.setFullName(nome);
        user.setRole(Role.USER);
        user.setIsActive(ativo);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}