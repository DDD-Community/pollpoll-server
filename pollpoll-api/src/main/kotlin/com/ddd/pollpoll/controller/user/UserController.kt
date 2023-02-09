package com.ddd.pollpoll.controller.user

import com.ddd.pollpoll.controller.SuccessResponse
import com.ddd.pollpoll.controller.user.dto.HasNicknameResponse
import com.ddd.pollpoll.controller.user.dto.MyPageResponse
import com.ddd.pollpoll.controller.user.dto.MyPageType
import com.ddd.pollpoll.controller.user.dto.UpdateNicknameRequest
import com.ddd.pollpoll.service.user.UserCommandService
import com.ddd.pollpoll.service.user.UserQueryService
import com.ddd.pollpoll.util.getSocialId
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "사용자")
@RequestMapping("/api/user")
@RestController
class UserController(
    private val userCommandService: UserCommandService,
    private val userQueryService: UserQueryService,
) {
    @Operation(summary = "닉네임 존재여부 확인")
    @GetMapping("/has-nickname")
    fun hasNickname(@RequestHeader("Authorization") bearerToken: String): SuccessResponse<HasNicknameResponse> {
        val socialId = getSocialId(bearerToken)
        return SuccessResponse(userQueryService.hasNickname(socialId))
    }

    @Operation(summary = "닉네임 수정")
    @PutMapping("/nickname")
    fun update(@RequestHeader("Authorization") bearerToken: String, @RequestBody dto: UpdateNicknameRequest) {
        val socialId = getSocialId(bearerToken)
        userCommandService.updateNickname(socialId, dto)
    }

    @Operation(summary = "마이페이지")
    @GetMapping("/my-page")
    fun myPage(
        @RequestHeader("Authorization") bearerToken: String,
        @RequestParam type: MyPageType,
        @RequestParam lastPostId: Long?,
        @RequestParam showOnlyInProgress: Boolean = false,
    ): SuccessResponse<MyPageResponse> {
        val socialId = getSocialId(bearerToken)
        return SuccessResponse(
            userQueryService.getMyPageWithShowMorePosts(
                socialId,
                type,
                lastPostId,
                showOnlyInProgress
            )
        )
    }
}
