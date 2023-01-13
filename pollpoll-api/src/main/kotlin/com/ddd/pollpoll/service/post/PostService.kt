package com.ddd.pollpoll.service.post

import com.ddd.pollpoll.controller.post.dto.CreatePostRequest
import com.ddd.pollpoll.controller.post.dto.PollItemDto
import com.ddd.pollpoll.controller.post.dto.PostPollResponse
import com.ddd.pollpoll.controller.post.dto.PostPollResponses
import com.ddd.pollpoll.domain.poll.Poll
import com.ddd.pollpoll.domain.poll.PollItem
import com.ddd.pollpoll.domain.post.Post
import com.ddd.pollpoll.repository.poll.PollItemRepository
import com.ddd.pollpoll.repository.poll.PollParticipantRepository
import com.ddd.pollpoll.repository.poll.PollRepository
import com.ddd.pollpoll.repository.poll.PollWatcherRepository
import com.ddd.pollpoll.repository.post.PostRepository
import com.ddd.pollpoll.repository.user.UserRepository
import com.ddd.pollpoll.service.category.CategoryService
import com.ddd.pollpoll.service.poll.PollService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class PostService(
    private val categoryService: CategoryService,
    private val pollService: PollService,

    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val pollRepository: PollRepository,
    private val pollItemRepository: PollItemRepository,
    private val pollParticipantRepository: PollParticipantRepository,
    private val pollWatcherRepository: PollWatcherRepository,
) {
    @Transactional
    fun create(socialId: String, dto: CreatePostRequest) {
        val user = userRepository.findBySocialId(socialId) ?: throw IllegalArgumentException("존재하지 않는 사용자입니다.")
        val category = categoryService.getCategory(dto.categoryId)
        val post = Post(category = category, title = dto.title, contents = dto.contents, user = user)
        val poll = Poll.of(post = post, isMultipleChoice = dto.multipleChoice, milliseconds = dto.milliseconds)
        val pollItems = getPollItems(dto.pollItems, poll)

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
        val postDtos = postRepository.getListByLastPostId(lastPostId)
        val postIds = postDtos.map { it.postId }
        val pollDtos = pollRepository.getListByPostIds(postIds)
        val pollIds = pollDtos.map { it.pollId }
        val pollParticipants = pollService.getPollParticipantsByPollIds(pollIds)
        val pollWatchers = pollService.getPollWatchersByPollIds(pollIds)

        val responses = postDtos.map {
            val pollDto = pollDtos.first { pollDto -> pollDto.postId == it.postId }
            val participantCount = pollParticipants[pollDto.pollId]?.size ?: 0
            val watcherCount = pollWatchers[pollDto.pollId]?.size ?: 0

            PostPollResponse.of(it, pollDto, participantCount, watcherCount)
        }
        return PostPollResponses(responses)
    }

    fun getPost(postId: Long): PostPollResponse {
        val postDto = postRepository.getOneById(postId) ?: throw RuntimeException("존재하지 않는 게시글입니다.")
        val pollDto = pollRepository.getOneByPostId(postId) ?: throw RuntimeException("존재하지 않는 투표입니다.")
        val participantCount = pollParticipantRepository.countByPoll_Id(pollDto.pollId)
        val watcherCount = pollWatcherRepository.countByPoll_Id(pollDto.pollId)

        return PostPollResponse.of(postDto, pollDto, participantCount, watcherCount)
    }
}
