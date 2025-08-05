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
        
        if (databaseUrl != null && databaseUrl.startsWith("postgresql://")) {
            // Converter URL do Render para formato JDBC
            databaseUrl = databaseUrl.replace("postgresql://", "jdbc:postgresql://");
        }
        
        return DataSourceBuilder
                .create()
                .url(databaseUrl)
                .driverClassName("org.postgresql.Driver")
                .username(System.getenv("DB_USERNAME"))
                .password(System.getenv("DB_PASSWORD"))
                .build();
    }
}