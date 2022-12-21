package com.ddd.pollpoll.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PollpollDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "pollpoll.datasource")
    fun pollpollHikariConfig(): HikariConfig {
        return HikariConfig()
    }

    @Bean
    fun pollpollDataSource(@Qualifier("pollpollHikariConfig") hikariConfig: HikariConfig): HikariDataSource {
        return HikariDataSource(hikariConfig)
    }
}
