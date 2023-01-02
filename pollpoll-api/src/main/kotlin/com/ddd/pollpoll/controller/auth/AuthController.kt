package com.ddd.pollpoll.controller.auth

import com.ddd.pollpoll.controller.auth.dto.AuthRequest
import com.ddd.pollpoll.controller.auth.dto.AuthResponse
import com.ddd.pollpoll.service.auth.GoogleAuthService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/auth")
@RestController
class AuthController(private val googleAuthService: GoogleAuthService) {

    @GetMapping("/callback")
    fun callback(): String = "callback ok"

    @PostMapping("/google-login")
    fun googleLogin(@RequestBody authRequest: AuthRequest): AuthResponse {
        return googleAuthService.login(authRequest)
    }
}
