package com.ddd.pollpoll.service.post

import com.ddd.pollpoll.controller.post.dto.PostPollResponse
import com.ddd.pollpoll.controller.post.dto.PostPollResponses
import com.ddd.pollpoll.repository.poll.PollParticipantRepository
import com.ddd.pollpoll.repository.poll.PollRepository
import com.ddd.pollpoll.repository.poll.PollWatcherRepository
import com.ddd.pollpoll.repository.post.PostDto
import com.ddd.pollpoll.repository.post.PostRepository
import com.ddd.pollpoll.service.poll.PollQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class PostQueryService(
    private val pollQueryService: PollQueryService,
    private val postRepository: PostRepository,
    private val pollRepository: PollRepository,
    private val pollParticipantRepository: PollParticipantRepository,
    private val pollWatcherRepository: PollWatcherRepository,
) {
    fun getShowMorePosts(lastPostId: Long, keyword: String?): PostPollResponses {
        val postDtos = postRepository.getListByLastPostIdAndKeyword(lastPostId, keyword)
        if (postDtos.isEmpty()) {
            return PostPollResponses.empty()
        }
        return getPostPollResponses(postDtos)
    }

    private fun getPostPollResponses(postDtos: List<PostDto>): PostPollResponses {
        val postIds = postDtos.map { it.postId }
        val pollDtos = pollRepository.getListByPostIds(postIds)
        val pollIds = pollDtos.map { it.pollId }
        val pollParticipants = pollQueryService.getPollParticipantsByPollIds(pollIds)
        val pollWatchers = pollQueryService.getPollWatchersByPollIds(pollIds)

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
        val participantCount = pollParticipantRepository.countByPollId(pollDto.pollId)
        val watcherCount = pollWatcherRepository.countByPollId(pollDto.pollId)

        return PostPollResponse.of(postDto, pollDto, participantCount, watcherCount)
    }
}
