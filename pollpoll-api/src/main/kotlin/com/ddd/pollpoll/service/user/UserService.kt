package com.ddd.pollpoll.service.user

import com.ddd.pollpoll.controller.user.dto.HasNicknameResponse
import com.ddd.pollpoll.controller.user.dto.UpdateNicknameRequest
import com.ddd.pollpoll.repository.user.UserRepository
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun hasNickname(socialId: String): HasNicknameResponse {
        val user = findBySocialId(socialId)
        return HasNicknameResponse(StringUtils.isNotEmpty(user.nickname))
    }

    @Transactional
    fun updateNickname(socialId: String, dto: UpdateNicknameRequest) {
        val user = findBySocialId(socialId)
        user.nickname = dto.nickname
    }

    private fun findBySocialId(socialId: String) =
        userRepository.findBySocialId(socialId) ?: throw RuntimeException("존재하지 않는 사용자입니다. (socialId: $socialId)")
}
