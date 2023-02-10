package com.ddd.pollpoll.service.post

import com.ddd.pollpoll.controller.post.dto.CreatePostRequest
import com.ddd.pollpoll.controller.post.dto.PollItemDto
import com.ddd.pollpoll.domain.poll.Poll
import com.ddd.pollpoll.domain.poll.PollItem
import com.ddd.pollpoll.domain.post.Post
import com.ddd.pollpoll.exception.ErrorCode
import com.ddd.pollpoll.exception.ErrorCode.FORBIDDEN_TO_DELETE_POST
import com.ddd.pollpoll.exception.ErrorCode.MINIMUM_POLL_ITEM_COUNT
import com.ddd.pollpoll.exception.ErrorCode.NOT_FOUND_POLL
import com.ddd.pollpoll.exception.PollpollException
import com.ddd.pollpoll.repository.poll.PollItemRepository
import com.ddd.pollpoll.repository.poll.PollParticipantRepository
import com.ddd.pollpoll.repository.poll.PollRepository
import com.ddd.pollpoll.repository.poll.PollWatcherRepository
import com.ddd.pollpoll.repository.post.PostHitsRepository
import com.ddd.pollpoll.repository.post.PostRepository
import com.ddd.pollpoll.service.category.CategoryQueryService
import com.ddd.pollpoll.service.user.UserQueryService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class PostCommandService(
    private val userQueryService: UserQueryService,
    private val categoryQueryService: CategoryQueryService,
    private val postRepository: PostRepository,
    private val postHitsRepository: PostHitsRepository,
    private val pollRepository: PollRepository,
    private val pollItemRepository: PollItemRepository,
    private val participantRepository: PollParticipantRepository,
    private val watcherRepository: PollWatcherRepository,
) {
    fun create(socialId: String, dto: CreatePostRequest) {
        val user = userQueryService.getUserBySocialId(socialId)
        val category = categoryQueryService.getCategory(dto.categoryId)
        val post = Post(category = category, title = dto.title, contents = dto.contents, user = user)
        val poll = Poll.of(post = post, isMultipleChoice = dto.multipleChoice, milliseconds = dto.milliseconds)
        val pollItems = getPollItems(dto.pollItems, poll)

        postRepository.save(post)
        pollRepository.save(poll)
        pollItemRepository.saveAll(pollItems)
    }

    fun deletePost(socialId: String, postId: Long) {
        val user = userQueryService.getUserBySocialId(socialId)
        val post = postRepository.findByIdOrNull(postId) ?: throw PollpollException(ErrorCode.NOT_FOUND_POST)
        if (user.id != post.user.id) {
            throw PollpollException(FORBIDDEN_TO_DELETE_POST)
        }
        val poll = pollRepository.findByPostId(postId) ?: throw PollpollException(NOT_FOUND_POLL)

        participantRepository.softDeleteByPollId(poll.id)
        watcherRepository.softDeleteByPollId(poll.id)
        pollItemRepository.softDeleteByPollId(poll.id)
        postHitsRepository.softDeleteByPostId(post.id)
        poll.delete()
        post.delete()
    }

    private fun getPollItems(
        pollItemDtos: List<PollItemDto>,
        poll: Poll
    ): List<PollItem> {
        return pollItemDtos
            .map { it.toEntity(poll) }
            .takeIf { it.size >= 2 }
            ?: throw PollpollException(MINIMUM_POLL_ITEM_COUNT)
    }
}
