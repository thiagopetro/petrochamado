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
        String dbUsername = System.getenv("DB_USERNAME");
        String dbPassword = System.getenv("DB_PASSWORD");
        
        // Validar se as variáveis de ambiente estão definidas
        if (databaseUrl == null || dbUsername == null || dbPassword == null) {
            throw new IllegalStateException("Variáveis de ambiente do banco de dados não configuradas: DATABASE_URL, DB_USERNAME, DB_PASSWORD");
        }
        
        if (databaseUrl.startsWith("postgresql://")) {
            // Converter URL do Render para formato JDBC
            databaseUrl = databaseUrl.replace("postgresql://", "jdbc:postgresql://");
        }
        
        return DataSourceBuilder
                .create()
                .url(databaseUrl)
                .driverClassName("org.postgresql.Driver")
                .username(dbUsername)
                .password(dbPassword)
                .build();
    }
}