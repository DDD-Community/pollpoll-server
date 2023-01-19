package com.ddd.pollpoll.service.poll

import com.ddd.pollpoll.controller.poll.dto.ParticipantRequest
import com.ddd.pollpoll.domain.poll.Poll
import com.ddd.pollpoll.domain.poll.PollParticipant
import com.ddd.pollpoll.domain.user.User
import com.ddd.pollpoll.repository.poll.PollParticipantRepository
import com.ddd.pollpoll.service.user.UserQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class PollCommandService(
    private val userQueryService: UserQueryService,
    private val pollQueryService: PollQueryService,
    private val pollParticipantRepository: PollParticipantRepository,
) {
    fun participant(
        socialId: String,
        pollId: Long,
        dto: ParticipantRequest,
    ) {
        val user = userQueryService.getUserBySocialId(socialId)
        val poll = pollQueryService.getPollByPollId(pollId)

        deletePreviousPollParticipants(user, poll)
        savePollParticipants(user, poll, dto.pollItemIds)
    }

    private fun deletePreviousPollParticipants(
        user: User,
        poll: Poll,
    ) {
        val pollParticipants = pollParticipantRepository.findByUserIdAndPollId(userId = user.id, pollId = poll.id)
        pollParticipants.forEach { it.delete() }
    }

    private fun savePollParticipants(
        user: User,
        poll: Poll,
        selectedPollItemIds: List<Long>,
    ) {
        val selectedPollItems = poll.pollItems.filter { selectedPollItemIds.contains(it.id) }
        require(selectedPollItems.size == selectedPollItemIds.size) { "투표 항목이 변경되었습니다. 새로고침 후 투표해주세요." }

        for (selectedPollItem in selectedPollItems) {
            pollParticipantRepository.save(PollParticipant(user, poll, selectedPollItem))
        }
    }
}
