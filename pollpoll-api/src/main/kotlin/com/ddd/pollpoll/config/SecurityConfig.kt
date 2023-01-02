package com.ddd.pollpoll.config

import com.ddd.pollpoll.filter.JwtRequestFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
@Configuration
class SecurityConfig(private val jwtRequestFilter: JwtRequestFilter) {

    companion object {
        val WHITELIST_WEB_URL: Array<String> = arrayOf(
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/",
            "/callback",
        )

        val WHITELIST_HTTP_URL: Array<String> = arrayOf(
            "/api/auth/**",
            "/health",
        )
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager;
    }

    // configure HttpSecurity (deprecated) -> WebSecurityCustomizer Bean
    @Bean
    fun configure(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web ->
            web.ignoring().antMatchers(*WHITELIST_WEB_URL)
        }
    }

    // configure WebSecurity (deprecated) -> SecurityFilterChain Bean
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf().disable()
            .cors() // todo: restrict allow origin
            .and()
            .authorizeRequests()
            .antMatchers(*WHITELIST_HTTP_URL).permitAll()
            .anyRequest().authenticated()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }
}
