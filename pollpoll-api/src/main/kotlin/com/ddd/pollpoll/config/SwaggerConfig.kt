package com.ddd.pollpoll.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.servers.Server
import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@OpenAPIDefinition(
    servers = [Server(url = "/")],
    info = Info(title = "폴폴 API", version = "v1")
)
@Configuration
class SwaggerConfig {

    @Bean
    fun groupedOpenApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("pollpoll-api")
            .pathsToMatch("/api/**")
            .build()
    }
}
