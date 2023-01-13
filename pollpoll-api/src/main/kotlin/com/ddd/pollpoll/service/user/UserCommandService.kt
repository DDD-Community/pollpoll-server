package com.ddd.pollpoll.service.user

import com.ddd.pollpoll.controller.user.dto.UpdateNicknameRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class UserCommandService(
    private val userQueryService: UserQueryService
) {
    fun updateNickname(socialId: String, dto: UpdateNicknameRequest) {
        val user = userQueryService.getUserBySocialId(socialId)
        user.nickname = dto.nickname
    }
}
