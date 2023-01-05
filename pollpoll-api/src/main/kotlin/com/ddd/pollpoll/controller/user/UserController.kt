package com.ddd.pollpoll.controller.user

import com.ddd.pollpoll.controller.SuccessResponse
import com.ddd.pollpoll.controller.user.dto.HasNicknameResponse
import com.ddd.pollpoll.controller.user.dto.UpdateNicknameRequest
import com.ddd.pollpoll.service.user.UserService
import com.ddd.pollpoll.util.getSocialId
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "사용자")
@RequestMapping("/api/user")
@RestController
class UserController(
    private val userService: UserService
) {
    @Operation(summary = "닉네임 존재여부 확인")
    @GetMapping("/has-nickname")
    fun hasNickname(@RequestHeader("Authorization") bearerToken: String): SuccessResponse<HasNicknameResponse> {
        val socialId = getSocialId(bearerToken)
        return SuccessResponse(userService.hasNickname(socialId))
    }

    @Operation(summary = "닉네임 수정")
    @PutMapping("/nickname")
    fun update(@RequestHeader("Authorization") bearerToken: String, @RequestBody dto: UpdateNicknameRequest) {
        val socialId = getSocialId(bearerToken)
        userService.updateNickname(socialId, dto)
    }
}
