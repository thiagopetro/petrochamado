package com.lovablepetro.chamadopetro.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("prod")
public class DatabaseConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        String databaseUrl = System.getenv("DATABASE_URL");
        
        // Validar se a variável de ambiente está definida
        if (databaseUrl == null) {
            throw new IllegalStateException("Variável de ambiente DATABASE_URL não configurada");
        }
        
        String jdbcUrl;
        String username;
        String password;
        
        if (databaseUrl.startsWith("postgresql://")) {
            // Extrair credenciais da URL do Render
            // Formato: postgresql://user:password@host:port/database
            try {
                // Remover o prefixo postgresql://
                String urlWithoutPrefix = databaseUrl.substring(13);
                
                // Encontrar a posição do @ que separa credenciais do host
                int atIndex = urlWithoutPrefix.indexOf('@');
                if (atIndex == -1) {
                    throw new IllegalArgumentException("URL do banco inválida: não contém @");
                }
                
                // Extrair credenciais (user:password)
                String credentials = urlWithoutPrefix.substring(0, atIndex);
                int colonIndex = credentials.indexOf(':');
                if (colonIndex == -1) {
                    throw new IllegalArgumentException("URL do banco inválida: credenciais sem :");
                }
                
                username = credentials.substring(0, colonIndex);
                password = credentials.substring(colonIndex + 1);
                
                // Extrair host:port/database
                String hostAndDatabase = urlWithoutPrefix.substring(atIndex + 1);
                
                // Construir URL JDBC
                jdbcUrl = "jdbc:postgresql://" + hostAndDatabase;
                
            } catch (Exception e) {
                throw new IllegalStateException("Erro ao processar DATABASE_URL: " + e.getMessage(), e);
            }
        } else {
            // Se já estiver no formato JDBC, usar variáveis separadas
            jdbcUrl = databaseUrl;
            username = System.getenv("DB_USERNAME");
            password = System.getenv("DB_PASSWORD");
            
            if (username == null || password == null) {
                throw new IllegalStateException("Para URLs JDBC, DB_USERNAME e DB_PASSWORD devem estar configurados");
            }
        }
        
        return DataSourceBuilder
                .create()
                .url(jdbcUrl)
                .driverClassName("org.postgresql.Driver")
                .username(username)
                .password(password)
                .build();
    }
}