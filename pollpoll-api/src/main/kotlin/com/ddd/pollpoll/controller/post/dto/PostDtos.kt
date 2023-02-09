package com.ddd.pollpoll.controller.post.dto

import com.ddd.pollpoll.domain.poll.Poll
import com.ddd.pollpoll.domain.poll.PollItem
import com.ddd.pollpoll.repository.poll.PollDto
import com.ddd.pollpoll.repository.post.PostDto
import java.time.ZoneId

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

data class PollItemResponseDto(
    val postItemId: Long,
    val name: String,
    val count: Long,
)

data class PostPollResponses(
    val posts: List<PostPollResponse>
) {
    companion object {
        fun empty(): PostPollResponses {
            return PostPollResponses(emptyList())
        }
    }
}

data class PostPollResponse(
    val postId: Long,
    val title: String?,
    val contents: String,
    val postCreatedAt: Long,
    val postHits: Int,
    val nickname: String,
    val categoryName: String,
    val pollId: Long,
    val pollEndAt: Long,
    val pollItemCount: Int,
    val pollItems: List<PollItemResponseDto>?,
    val participantCount: Int,
    val watcherCount: Int,
) {
    companion object {
        fun of(
            postDto: PostDto,
            pollDto: PollDto,
            pollItemDtos: List<PollItemResponseDto>?,
            participantCount: Int,
            watcherCount: Int,
        ): PostPollResponse {
            return PostPollResponse(
                postId = postDto.postId,
                title = postDto.postTitle,
                contents = postDto.postContents,
                postCreatedAt = postDto.postCreatedAt.atZone(ZoneId.of("Asia/Seoul")).toInstant()?.toEpochMilli() ?: 0,
                postHits = postDto.postHits,
                nickname = postDto.nickname,
                categoryName = postDto.categoryName,
                pollId = pollDto.pollId,
                pollEndAt = pollDto.pollEndAt.atZone(ZoneId.of("Asia/Seoul")).toInstant()?.toEpochMilli() ?: 0,
                pollItemCount = pollDto.pollItemCount,
                pollItems = pollItemDtos,
                participantCount = participantCount,
                watcherCount = watcherCount
            )
        }
    }
}

data class PopularPostsResponse(
    val mostParticipatePost: PostPollResponse?,
    val mostWatchPost: PostPollResponse?,
    val endingSoonPost: PostPollResponse?,
)
