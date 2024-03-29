package com.ddd.pollpoll.service.post

import com.ddd.pollpoll.controller.post.dto.PollItemResponseDto
import com.ddd.pollpoll.controller.post.dto.PopularPostsResponse
import com.ddd.pollpoll.controller.post.dto.PostPollResponse
import com.ddd.pollpoll.controller.post.dto.PostPollResponses
import com.ddd.pollpoll.exception.ErrorCode
import com.ddd.pollpoll.exception.ErrorCode.NOT_FOUND_POLL
import com.ddd.pollpoll.exception.ErrorCode.NOT_FOUND_POST
import com.ddd.pollpoll.exception.PollpollException
import com.ddd.pollpoll.repository.poll.PollItemRepository
import com.ddd.pollpoll.repository.poll.PollParticipantRepository
import com.ddd.pollpoll.repository.poll.PollRepository
import com.ddd.pollpoll.repository.poll.PollWatcherRepository
import com.ddd.pollpoll.repository.post.PostDto
import com.ddd.pollpoll.repository.post.PostRepository
import com.ddd.pollpoll.repository.user.UserRepository
import com.ddd.pollpoll.service.poll.PollQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class PostQueryService(
    private val pollQueryService: PollQueryService,
    private val postHitsCommandService: PostHitsCommandService,
    private val postRepository: PostRepository,
    private val pollRepository: PollRepository,
    private val pollItemRepository: PollItemRepository,
    private val pollParticipantRepository: PollParticipantRepository,
    private val pollWatcherRepository: PollWatcherRepository,
    private val userRepository: UserRepository
) {
    fun getShowMorePosts(lastPostId: Long?, keyword: String?, categoryId: Long?): PostPollResponses {
        val postDtos = postRepository.getListByLastPostIdAndKeyword(lastPostId, keyword, categoryId)
        if (postDtos.isEmpty()) {
            return PostPollResponses.empty()
        }
        val responses = toResponses(postDtos)
        return PostPollResponses(responses)
    }

    fun getShowMoreMyPostsByUserId(lastPostId: Long?, userId: Long): List<PostPollResponse> {
        val postDtos = postRepository.getMyPostsByLastPostIdAndUserId(lastPostId, userId)
        if (postDtos.isEmpty()) {
            return emptyList()
        }
        return toResponses(postDtos)
    }


    fun getShowMoreParticipatePostsByUserId(lastPostId: Long?, userId: Long): List<PostPollResponse> {
        val postDtos = postRepository.getParticipatePostsByLastPostIdAndUserId(lastPostId, userId)
        if (postDtos.isEmpty()) {
            return emptyList()
        }
        return toResponses(postDtos)
    }

    fun getShowMoreWatchPostsByUserId(lastPostId: Long?, userId: Long): List<PostPollResponse> {
        val postDtos = postRepository.getWatchPostsByLastPostIdAndUser(lastPostId, userId)
        if (postDtos.isEmpty()) {
            return emptyList()
        }
        return toResponses(postDtos)
    }

    private fun toResponses(postDtos: List<PostDto>): List<PostPollResponse> {
        val postIds = postDtos.map { it.postId }
        val pollDtos = pollRepository.getListByPostIds(postIds)
        val pollIds = pollDtos.map { it.pollId }
        val pollParticipants = pollQueryService.getPollParticipantsByPollIds(pollIds)
        val pollWatchers = pollQueryService.getPollWatchersByPollIds(pollIds)

        return postDtos.map {
            val pollDto = pollDtos.first { pollDto -> pollDto.postId == it.postId }
            val participantCount = pollParticipants[pollDto.pollId]?.size ?: 0
            val watcherCount = pollWatchers[pollDto.pollId]?.size ?: 0

            PostPollResponse.of(it, pollDto, null, participantCount, watcherCount)
        }
    }

    @Transactional
    fun getPost(socialId: String?, postId: Long): PostPollResponse {
        if (socialId != null) {
            postHitsCommandService.increase(socialId, postId)
        }
        val postDto = postRepository.getOneById(postId) ?: throw PollpollException(NOT_FOUND_POST)
        val pollDto = pollRepository.getOneByPostId(postId) ?: throw PollpollException(NOT_FOUND_POLL)
        val pollItems = if (socialId == null) {
            pollItemRepository.findByPollId(pollDto.pollId)
        } else {
            val user = userRepository.findBySocialId(socialId) ?: throw PollpollException(ErrorCode.NOT_FOUND_USER)
            pollItemRepository.findBySocialIdAndPollId(user.id, pollDto.pollId)
        }
        val participantCount = pollParticipantRepository.countByPollId(pollDto.pollId)
        val pollItemParticipateCounts = pollParticipantRepository.getParticipateCountByPollId(pollDto.pollId)
        val watcherCount = pollWatcherRepository.countByPollId(pollDto.pollId)

        return PostPollResponse.of(
            postDto,
            pollDto,
            pollItems.map {
                PollItemResponseDto(
                    pollItemId = it.pollItemId,
                    name = it.name,
                    count = pollItemParticipateCounts.first { el -> el.pollItemId == it.pollItemId }.count,
                    isPolled = it.isPolled
                )
            },
            participantCount,
            watcherCount
        )
    }

    fun getPopularPosts(): PopularPostsResponse {
        val mostParticipatePostId = postRepository.getMostParticipatePost()?.postId
        val mostWatchPostId = postRepository.getMostWatchPost()?.postId
        val endingSoonPostId = postRepository.getEndingSoonPostId()

        return PopularPostsResponse(
            mostParticipatePost = if (mostParticipatePostId == null) null else getPost(null, mostParticipatePostId),
            mostWatchPost = if (mostWatchPostId == null) null else getPost(null, mostWatchPostId),
            endingSoonPost = if (endingSoonPostId == null) null else getPost(null, endingSoonPostId),
        )
    }
}
