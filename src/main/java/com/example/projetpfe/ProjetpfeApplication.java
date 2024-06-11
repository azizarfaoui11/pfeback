package com.example.projetpfe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableAsync
@EnableWebSecurity
@SpringBootApplication
public class ProjetpfeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjetpfeApplication.class, args);
    }

}
