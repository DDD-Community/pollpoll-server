package com.ddd.pollpoll.service.auth

import com.ddd.pollpoll.client.GoogleClient
import com.ddd.pollpoll.controller.auth.dto.AuthRequest
import com.ddd.pollpoll.controller.auth.dto.AuthResponse
import com.ddd.pollpoll.domain.user.User
import com.ddd.pollpoll.repository.user.UserRepository
import com.ddd.pollpoll.util.createJwt
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class GoogleAuthService(
    private val googleClient: GoogleClient,
    private val userRepository: UserRepository
) {
    @Transactional
    fun login(authRequest: AuthRequest): AuthResponse {
        val tokenInfoDto = googleClient.getTokenInfo(authRequest.idToken)
        val socialId = tokenInfoDto.socialId

        val user: User? = userRepository.findBySocialId(socialId)
        if (user == null) {
            userRepository.save(tokenInfoDto.toUser())
        }
        val accessToken = createJwt(socialId)
        return AuthResponse(accessToken)
    }
}
