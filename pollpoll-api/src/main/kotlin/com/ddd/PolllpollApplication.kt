package com.ddd

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class PolllpollApplication

fun main(args: Array<String>) {
    runApplication<PolllpollApplication>(*args)
}
