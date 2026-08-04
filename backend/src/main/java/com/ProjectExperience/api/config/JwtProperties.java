package com.ProjectExperience.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    /**
     * Chave secreta utilizada para assinar o token JWT.
     */
    private String secret;

    /**
     * Tempo de expiração do token em milissegundos.
     */
    private Long expirationMs;
}