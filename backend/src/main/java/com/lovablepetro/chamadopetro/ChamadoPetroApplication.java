package com.lovablepetro.chamadopetro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ChamadoPetroApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(ChamadoPetroApplication.class, args);
        } catch (Throwable e) {
            System.err.println("Erro fatal durante a inicialização da aplicação:");
            e.printStackTrace();
            System.exit(1);
        }
    }

}