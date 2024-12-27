package com.eCommerce.configuration;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI().info(new Info().title("eCommerce API").version("1.0").description("API documentation for eCommerce project"));
  }

  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder().group("public").pathsToMatch("/**") // Incluye todos los endpoints REST
        .packagesToExclude("com.eCommerce.exception.handler") // Excluye el paquete del RestControllerAdvice
        .build();
  }
}