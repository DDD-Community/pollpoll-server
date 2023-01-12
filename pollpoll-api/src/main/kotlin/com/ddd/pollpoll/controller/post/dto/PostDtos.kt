package com.ddd.pollpoll.controller.post.dto

import com.ddd.pollpoll.domain.poll.Poll
import com.ddd.pollpoll.domain.poll.PollItem

data class CreatePostRequest(
    val categoryId: Long,
    val title: String,
    val contents: String,
    val pollItems: List<PollItemDto>,
    val multipleChoice: Boolean,
    val milliseconds: Long,
)

data class PollItemDto(
    val name: String,
) {
    fun toEntity(poll: Poll): PollItem {
        return PollItem(poll, name)
    }
}

data class PostPollResponses(val posts: List<PostPollResponse>)

data class PostPollResponse(
    val postId: Long,
    val title: String,
    val contents: String,
    val pollEndAt: Long,
    val pollItemCount: Int,
    val participantCount: Int,
    val watcherCount: Int,
)

data class PostPollResponse2(
    val postId: Long,
    val title: String,
    val contents: String,
    val postCreatedAt: Long?,
    val postHits: Int?,
    val nickname: String?,
    val categoryName: String,
    val pollId: Long,
    val pollEndAt: Long,
    val pollItemCount: Int,
    val participantCount: Int,
    val watcherCount: Int,
)
