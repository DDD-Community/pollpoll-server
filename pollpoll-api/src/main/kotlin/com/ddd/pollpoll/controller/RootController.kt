package com.ddd.pollpoll.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RootController {

    @GetMapping("/health")
    fun health(): String = "ok"

    @GetMapping("/callback")
    fun callback(): String = "ok"
}
