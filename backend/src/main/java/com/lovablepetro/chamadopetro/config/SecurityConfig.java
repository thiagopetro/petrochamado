package com.lovablepetro.chamadopetro.config;

import com.lovablepetro.chamadopetro.service.AuthService;
import com.lovablepetro.chamadopetro.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private AuthService authService;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        
        return http.build();
    }
    
    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        
        // Configurar origins baseado no ambiente
        String allowedOrigins = System.getenv("FRONTEND_URL");
        if (allowedOrigins != null && !allowedOrigins.isEmpty()) {
            // Produção: usar URL do frontend do Render
            configuration.setAllowedOriginPatterns(java.util.Arrays.asList(allowedOrigins, "https://*.onrender.com"));
        } else {
            // Desenvolvimento: usar localhost, ngrok e localtunnel
            configuration.setAllowedOriginPatterns(java.util.Arrays.asList(
                "http://localhost:3000", 
                "http://localhost:3001",
                "https://*.ngrok-free.app",
                "https://*.ngrok.app",
                "https://*.ngrok.io",
                "https://*.loca.lt"
            ));
        }
        
        configuration.setAllowedMethods(java.util.Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(java.util.Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(authService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}