package com.ddd.pollpoll.service.post

import com.ddd.pollpoll.controller.post.dto.CreatePostRequest
import com.ddd.pollpoll.controller.post.dto.PollItemDto
import com.ddd.pollpoll.controller.post.dto.PostPollResponse
import com.ddd.pollpoll.controller.post.dto.PostPollResponses
import com.ddd.pollpoll.domain.poll.Poll
import com.ddd.pollpoll.domain.poll.PollItem
import com.ddd.pollpoll.domain.poll.PollParticipant
import com.ddd.pollpoll.domain.poll.PollWatcher
import com.ddd.pollpoll.domain.post.Post
import com.ddd.pollpoll.repository.poll.PollItemRepository
import com.ddd.pollpoll.repository.poll.PollParticipantRepository
import com.ddd.pollpoll.repository.poll.PollRepository
import com.ddd.pollpoll.repository.poll.PollWatcherRepository
import com.ddd.pollpoll.repository.post.PostRepository
import com.ddd.pollpoll.service.category.CategoryService
import org.springframework.stereotype.Service
import java.time.ZoneId

@Service
class PostService(
    private val categoryService: CategoryService,
    private val postRepository: PostRepository,
    private val pollRepository: PollRepository,
    private val pollItemRepository: PollItemRepository,
    private val pollParticipantRepository: PollParticipantRepository,
    private val pollWatcherRepository: PollWatcherRepository,
) {
    fun create(socialId: String, dto: CreatePostRequest) {
        val category = categoryService.getCategory(dto.categoryId)
        val post = Post(category = category, title = dto.title, contents = dto.contents)

        val pollDto = dto.pollDto
        val poll = Poll.of(post = post, isMultipleChoice = pollDto.multipleChoice, milliseconds = pollDto.milliseconds)

        val pollItemDtos = pollDto.pollItemDtos
        val pollItems = getPollItems(pollItemDtos, poll)

        postRepository.save(post)
        pollRepository.save(poll)
        pollItemRepository.saveAll(pollItems)
    }

    private fun getPollItems(
        pollItemDtos: List<PollItemDto>,
        poll: Poll
    ): List<PollItem> {
        return pollItemDtos
            .map { it.toEntity(poll) }
            .takeIf { it.size >= 2 }
            ?: throw RuntimeException("투표 항목은 2개 이상이어야 합니다.")
    }

    fun getShowMoreList(lastPostId: Long): PostPollResponses {
        val postPollDtos = postRepository.findByLastPostId(lastPostId)
        val pollIds = postPollDtos.map { it.pollId }
        val pollParticipants = getPollParticipants(pollIds)
        val pollWatchers = getPollWatchers(pollIds)

        val responses = postPollDtos.map {
            val participantCount = pollParticipants[it.pollId]?.size ?: 0
            val watcherCount = pollWatchers[it.pollId]?.size ?: 0

            PostPollResponse(
                postId = it.postId,
                title = it.postTitle,
                contents = it.postContents,
                pollEndAt = it.pollEndAt.atZone(ZoneId.of("Asia/Seoul")).toInstant()?.toEpochMilli() ?: 0,
                pollItemCount = it.pollItemCount,
                participantCount = participantCount,
                watcherCount = watcherCount,
            )
        }
        return PostPollResponses(responses)
    }

    private fun getPollParticipants(pollIds: List<Long>): Map<Long, List<PollParticipant>> {
        val participants = pollParticipantRepository.findByPollIds(pollIds)
        return participants.groupBy { it.poll.id }
    }

    private fun getPollWatchers(pollIds: List<Long>): Map<Long, List<PollWatcher>> {
        val watchers = pollWatcherRepository.findByPollIds(pollIds)
        return watchers.groupBy { it.poll.id }
    }
}
