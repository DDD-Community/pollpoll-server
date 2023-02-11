package com.ddd.pollpoll.service.post

import com.ddd.pollpoll.domain.post.PostHits
import com.ddd.pollpoll.exception.ErrorCode.NOT_FOUND_POST
import com.ddd.pollpoll.exception.ErrorCode.NOT_FOUND_USER
import com.ddd.pollpoll.exception.PollpollException
import com.ddd.pollpoll.repository.post.PostHitsRepository
import com.ddd.pollpoll.repository.post.PostRepository
import com.ddd.pollpoll.repository.user.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class PostHitsCommandService(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val postHitsRepository: PostHitsRepository,
) {
    fun increase(socialId: String, postId: Long) {
        val user = userRepository.findBySocialId(socialId) ?: throw PollpollException(NOT_FOUND_USER)
        val post = postRepository.findByIdOrNull(postId) ?: throw PollpollException(NOT_FOUND_POST)

        postHitsRepository.findByPostIdAndUserId(postId = post.id, userId = user.id)?.renewUpdatedAt()
            ?: postHitsRepository.save(PostHits(user, post))
    }
}
