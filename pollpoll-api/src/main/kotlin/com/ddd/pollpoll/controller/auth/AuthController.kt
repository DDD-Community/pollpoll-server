package com.ddd.pollpoll.controller.auth

import com.ddd.pollpoll.controller.SuccessResponse
import com.ddd.pollpoll.controller.auth.dto.AuthRequest
import com.ddd.pollpoll.controller.auth.dto.AuthResponse
import com.ddd.pollpoll.service.auth.GoogleAuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "로그인")
@RequestMapping("/api/auth")
@RestController
class AuthController(private val googleAuthService: GoogleAuthService) {

    @Operation(summary = "구글 로그인")
    @PostMapping("/google-login")
    fun googleLogin(@RequestBody authRequest: AuthRequest): SuccessResponse<AuthResponse> {
        return SuccessResponse(googleAuthService.login(authRequest))
    }
}
