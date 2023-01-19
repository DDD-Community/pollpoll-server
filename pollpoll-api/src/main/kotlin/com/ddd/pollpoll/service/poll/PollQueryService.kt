package com.ddd.pollpoll.service.poll

import com.ddd.pollpoll.domain.poll.Poll
import com.ddd.pollpoll.domain.poll.PollParticipant
import com.ddd.pollpoll.domain.poll.PollWatcher
import com.ddd.pollpoll.repository.poll.PollParticipantRepository
import com.ddd.pollpoll.repository.poll.PollRepository
import com.ddd.pollpoll.repository.poll.PollWatcherRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class PollQueryService(
    private val pollRepository: PollRepository,
    private val pollParticipantRepository: PollParticipantRepository,
    private val pollWatcherRepository: PollWatcherRepository,
) {
    fun getPollByPollId(pollId: Long): Poll {
        return pollRepository.findByIdOrNull(pollId) ?: throw RuntimeException("존재하지 않는 투표입니다.")
    }

    fun getPollParticipantsByPollIds(pollIds: List<Long>): Map<Long, List<PollParticipant>> {
        val participants = pollParticipantRepository.getListByPollIds(pollIds)
        return participants.groupBy { it.poll.id }
    }

    fun getPollWatchersByPollIds(pollIds: List<Long>): Map<Long, List<PollWatcher>> {
        val watchers = pollWatcherRepository.getListByPollIds(pollIds)
        return watchers.groupBy { it.poll.id }
    }
}
