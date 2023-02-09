package com.ddd.pollpoll.controller.user.dto

import com.ddd.pollpoll.controller.post.dto.PostPollResponse

data class UpdateNicknameRequest(val nickname: String)

data class HasNicknameResponse(val hasNickname: Boolean)

data class RecommendNicknameResponse(val nickname: String)

data class MyPageResponse(
    val nickname: String,
    val myPollCount: Int,
    val participatePollCount: Int,
    val watchPollCount: Int,
    val posts: List<PostPollResponse>,
)

enum class MyPageType(
    val description: String,
) {
    MY_POLL("내가 쓴 투표"),
    PARTICIPATE_POLL("참여한 투표"),
    WATCH_POLL("구경한 투표"),
}
