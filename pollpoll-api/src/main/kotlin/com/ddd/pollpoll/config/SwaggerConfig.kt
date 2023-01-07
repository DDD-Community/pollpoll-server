package com.ddd.pollpoll.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.servers.Server
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders

@OpenAPIDefinition(
    servers = [Server(url = "/")],
    info = Info(title = "폴폴 API", version = "v1")
)
@Configuration
class SwaggerConfig {

    @Bean
    fun openApi(): OpenAPI {
        val securityScheme = SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("Bearer")
            .bearerFormat("JWT")
            .`in`(SecurityScheme.In.HEADER)
            .name(HttpHeaders.AUTHORIZATION)
        val securityRequirement = SecurityRequirement()
            .addList("BearerAuth")

        return OpenAPI()
            .components(Components().addSecuritySchemes("BearerAuth", securityScheme))
            .security(listOf(securityRequirement))
    }

    @Bean
    fun groupedOpenApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("pollpoll-api")
            .pathsToMatch("/api/**")
            .build()
    }
}
