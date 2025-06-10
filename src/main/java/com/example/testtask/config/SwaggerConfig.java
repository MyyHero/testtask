package com.example.testtask.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info      = @Info(title = "Test-Task API", version = "v1"),
        security  = @SecurityRequirement(name = "bearerAuth")   // ⬅ global
)
@SecurityScheme(
        name         = "bearerAuth",
        scheme       = "bearer",
        type         = SecuritySchemeType.HTTP,
        bearerFormat = "JWT"
)
public class SwaggerConfig {}
