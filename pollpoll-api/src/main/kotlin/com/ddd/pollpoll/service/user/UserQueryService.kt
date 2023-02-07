package com.ddd.pollpoll.service.user

import com.ddd.pollpoll.controller.user.dto.HasNicknameResponse
import com.ddd.pollpoll.controller.user.dto.MyPageResponse
import com.ddd.pollpoll.controller.user.dto.MyPageType
import com.ddd.pollpoll.controller.user.dto.MyPageType.MY_POLL
import com.ddd.pollpoll.controller.user.dto.MyPageType.PARTICIPATE_POLL
import com.ddd.pollpoll.controller.user.dto.MyPageType.WATCH_POLL
import com.ddd.pollpoll.repository.poll.PollParticipantRepository
import com.ddd.pollpoll.repository.poll.PollRepository
import com.ddd.pollpoll.repository.poll.PollWatcherRepository
import com.ddd.pollpoll.repository.user.UserRepository
import com.ddd.pollpoll.service.post.PostQueryService
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class UserQueryService(
    private val userRepository: UserRepository,
    private val postQueryService: PostQueryService,
    private val pollRepository: PollRepository,
    private val pollParticipantRepository: PollParticipantRepository,
    private val pollWatcherRepository: PollWatcherRepository,
) {
    fun hasNickname(socialId: String): HasNicknameResponse {
        val user = getUserBySocialId(socialId)
        return HasNicknameResponse(StringUtils.isNotEmpty(user.nickname))
    }

    fun getUserBySocialId(socialId: String) =
        userRepository.findBySocialId(socialId) ?: throw RuntimeException("존재하지 않는 사용자입니다. (socialId: $socialId)")

    fun getMyPageWithShowMorePosts(socialId: String, type: MyPageType, lastPostId: Long): MyPageResponse {
        val user = getUserBySocialId(socialId)
        val userId = user.id

        val myPollCount = pollRepository.countPollByUserId(userId = userId)
        val participatePollCount = pollParticipantRepository.countParticipatePollByUserId(userId = userId)
        val watchPollCount = pollWatcherRepository.countWatchPollByUserId(userId = userId)
        val posts = when (type) {
            MY_POLL -> postQueryService.getShowMoreMyPostsByUserId(lastPostId, userId)
            PARTICIPATE_POLL -> postQueryService.getShowMoreParticipatePostsByUserId(lastPostId, userId)
            WATCH_POLL -> postQueryService.getShowMoreWatchPostsByUserId(lastPostId, userId)
        }

        return MyPageResponse(
            nickname = user.nickname ?: "닉네임",
            myPollCount = myPollCount,
            participatePollCount = participatePollCount,
            watchPollCount = watchPollCount,
            posts = posts
        )
    }
}
