package com.ddd.pollpoll.service.poll

import com.ddd.pollpoll.controller.poll.dto.ParticipantRequest
import com.ddd.pollpoll.domain.poll.Poll
import com.ddd.pollpoll.domain.poll.PollParticipant
import com.ddd.pollpoll.domain.poll.PollWatcher
import com.ddd.pollpoll.domain.user.User
import com.ddd.pollpoll.repository.poll.PollParticipantRepository
import com.ddd.pollpoll.repository.poll.PollWatcherRepository
import com.ddd.pollpoll.service.user.UserQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class PollCommandService(
    private val userQueryService: UserQueryService,
    private val pollQueryService: PollQueryService,
    private val pollParticipantRepository: PollParticipantRepository,
    private val pollWatcherRepository: PollWatcherRepository,
) {
    fun participant(
        socialId: String,
        pollId: Long,
        dto: ParticipantRequest,
    ) {
        val user = userQueryService.getUserBySocialId(socialId)
        val poll = pollQueryService.getPollByPollId(pollId)

        deletePollWatch(poll, user)
        deletePreviousPollParticipants(poll, user)
        savePollParticipants(poll, dto.pollItemIds, user)
    }

    fun watch(
        socialId: String,
        pollId: Long,
    ) {
        val user = userQueryService.getUserBySocialId(socialId)
        val poll = pollQueryService.getPollByPollId(pollId)

        deletePreviousPollParticipants(poll, user)
        savePollWatch(user, poll)
    }

    private fun deletePreviousPollParticipants(
        poll: Poll,
        user: User,
    ) {
        val pollParticipants = pollParticipantRepository.findByUserIdAndPollId(userId = user.id, pollId = poll.id)
        pollParticipants.forEach { it.delete() }
    }

    private fun savePollParticipants(
        poll: Poll,
        selectedPollItemIds: List<Long>,
        user: User,
    ) {
        val selectedPollItems = poll.pollItems.filter { selectedPollItemIds.contains(it.id) }
        require(selectedPollItems.size == selectedPollItemIds.size) { "투표 항목이 변경되었습니다. 다시 투표해주세요." }

        for (selectedPollItem in selectedPollItems) {
            pollParticipantRepository.save(PollParticipant(user, poll, selectedPollItem))
        }
    }

    private fun deletePollWatch(
        poll: Poll,
        user: User,
    ) {
        val pollWatcher = pollWatcherRepository.findByPollAndUser(poll, user) ?: return
        pollWatcher.delete()
    }

    private fun savePollWatch(
        user: User,
        poll: Poll,
    ) {
        pollWatcherRepository.findByPollAndUser(poll, user) ?: pollWatcherRepository.save(PollWatcher(poll, user))
    }
}
