package com.ddd.pollpoll.service.post

import com.ddd.pollpoll.controller.post.dto.PollItemResponseDto
import com.ddd.pollpoll.controller.post.dto.PopularPostsResponse
import com.ddd.pollpoll.controller.post.dto.PostPollResponse
import com.ddd.pollpoll.controller.post.dto.PostPollResponses
import com.ddd.pollpoll.repository.poll.PollItemRepository
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
    private val pollItemRepository: PollItemRepository,
    private val pollParticipantRepository: PollParticipantRepository,
    private val pollWatcherRepository: PollWatcherRepository,
) {
    fun getShowMorePosts(lastPostId: Long, keyword: String?): PostPollResponses {
        val postDtos = postRepository.getListByLastPostIdAndKeyword(lastPostId, keyword)
        if (postDtos.isEmpty()) {
            return PostPollResponses.empty()
        }
        val responses = toResponses(postDtos)
        return PostPollResponses(responses)
    }

    fun getShowMoreMyPostsByUserId(lastPostId: Long, userId: Long): List<PostPollResponse> {
        val postDtos = postRepository.getMyPostsByLastPostIdAndUserId(lastPostId, userId)
        if (postDtos.isEmpty()) {
            return emptyList()
        }
        return toResponses(postDtos)
    }


    fun getShowMoreParticipatePostsByUserId(lastPostId: Long, userId: Long): List<PostPollResponse> {
        val postDtos = postRepository.getParticipatePostsByLastPostIdAndUserId(lastPostId, userId)
        if (postDtos.isEmpty()) {
            return emptyList()
        }
        return toResponses(postDtos)
    }

    fun getShowMoreWatchPostsByUserId(lastPostId: Long, userId: Long): List<PostPollResponse> {
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

    fun getPost(postId: Long): PostPollResponse {
        val postDto = postRepository.getOneById(postId) ?: throw RuntimeException("존재하지 않는 게시글입니다.")
        val pollDto = pollRepository.getOneByPostId(postId) ?: throw RuntimeException("존재하지 않는 투표입니다.")
        val pollItems = pollItemRepository.findByPollId(pollDto.pollId)
        val participantCount = pollParticipantRepository.countByPollId(pollDto.pollId)
        val pollItemParticipateCounts = pollParticipantRepository.getParticipateCountByPollId(pollDto.pollId)
        val watcherCount = pollWatcherRepository.countByPollId(pollDto.pollId)

        return PostPollResponse.of(
            postDto,
            pollDto,
            pollItems.map {
                PollItemResponseDto(
                    it.id,
                    it.name,
                    pollItemParticipateCounts.first { el -> el.pollItemId == it.id }.count
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
            mostParticipatePost = if (mostParticipatePostId == null) null else getPost(mostParticipatePostId),
            mostWatchPost = if (mostWatchPostId == null) null else getPost(mostWatchPostId),
            endingSoonPost = if (endingSoonPostId == null) null else getPost(endingSoonPostId),
        )
    }
}
