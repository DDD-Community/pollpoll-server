package com.ddd.pollpoll.controller.auth.dto

data class AuthRequest(val idToken: String)

data class AuthResponse(val accessToken: String)
