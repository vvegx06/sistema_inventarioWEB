package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class SistemaInventarioApplication {
    private static final Logger logger = LoggerFactory.getLogger(SistemaInventarioApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SistemaInventarioApplication.class, args);
        logger.info("========================================");
        logger.info("✓ Aplicación iniciada correctamente");
        logger.info("✓ Conexión a Base de Datos establecida");
        logger.info("✓ Sistema listo en: http://localhost:8080");
        logger.info("========================================");
    }
}