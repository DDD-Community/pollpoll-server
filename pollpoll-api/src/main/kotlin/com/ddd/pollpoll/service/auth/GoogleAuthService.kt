package com.ddd.pollpoll.service.auth

import GoogleUser
import com.ddd.pollpoll.controller.auth.dto.AuthRequest
import com.ddd.pollpoll.controller.auth.dto.AuthResponse
import com.ddd.pollpoll.domain.user.User
import com.ddd.pollpoll.repository.user.UserRepository
import com.ddd.pollpoll.util.JwtUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class GoogleAuthService(
//    private val googleClient: GoogleClient,
    private val userRepository: UserRepository
) {
    @Transactional
    fun login(authRequest: AuthRequest): AuthResponse {
//        val googleUser: GoogleUser = googleClient.getUser(authRequest.accessToken)
        val googleUser = GoogleUser("test", "1")
        val socialId: String = googleUser.socialId

        val user: User? = userRepository.findBySocialId(socialId)
        if (user == null) {
            userRepository.save(googleUser.toUser())
        }
        val accessToken: String = JwtUtil.createToken(socialId)
        return AuthResponse(accessToken)
    }
}
