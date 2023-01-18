package com.ddd.pollpoll.client

import com.ddd.pollpoll.domain.user.SocialType
import com.ddd.pollpoll.domain.user.User
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "GoogleClient", url = "https://oauth2.googleapis.com/tokeninfo")
interface GoogleClient {

    @GetMapping
    fun getTokenInfo(@RequestParam("id_token") idToken: String): TokenInfoDto
}

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TokenInfoDto(
    private val iss: String,
    private val azp: String,
    private val aud: String,
    private val sub: String,
    private val email: String,
    private val emailVerified: String,
    private val atHash: String?,
    private val iat: String,
    private val exp: String,
    private val alg: String?,
    private val kid: String?,
    private val typ: String?,
) {
    val socialId: String
        get() = sub

    fun toUser(): User {
        return User(
            socialType = SocialType.GOOGLE,
            socialId = sub,
            email = email,
        )
    }
}
