package com.example.eightyage.global.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class EnvConfig {
    @PostConstruct
    public void init() {
        Dotenv dotenv = Dotenv.load();

        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USER", dotenv.get("DB_USER"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("DB_URL", dotenv.get("AWS_ACCESS_KEY"));
        System.setProperty("DB_USER", dotenv.get("AWS_SECRET_KEY"));
        System.setProperty("DB_PASSWORD", dotenv.get("JWT_SECRET_KEY"));
    }
}
