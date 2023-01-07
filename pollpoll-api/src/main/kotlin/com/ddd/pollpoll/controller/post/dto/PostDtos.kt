package com.ddd.pollpoll.controller.post.dto

import com.ddd.pollpoll.domain.poll.Poll
import com.ddd.pollpoll.domain.poll.PollItem

data class CreatePostRequest(
    val categoryId: Long,
    val title: String,
    val contents: String,
    val pollDto: PollDto,
)

data class PollDto(
    val pollItemDtos: List<PollItemDto>,
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
