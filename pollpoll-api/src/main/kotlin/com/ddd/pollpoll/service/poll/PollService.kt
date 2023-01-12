package com.ddd.pollpoll.service.poll

import com.ddd.pollpoll.domain.poll.PollParticipant
import com.ddd.pollpoll.domain.poll.PollWatcher
import com.ddd.pollpoll.repository.poll.PollParticipantRepository
import com.ddd.pollpoll.repository.poll.PollWatcherRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class PollService(
    private val pollParticipantRepository: PollParticipantRepository,
    private val pollWatcherRepository: PollWatcherRepository,
) {
    fun getPollParticipantsByPollIds(pollIds: List<Long>): Map<Long, List<PollParticipant>> {
        val participants = pollParticipantRepository.getByPollIds(pollIds)
        return participants.groupBy { it.poll.id }
    }

    fun getPollWatchersByPollIds(pollIds: List<Long>): Map<Long, List<PollWatcher>> {
        val watchers = pollWatcherRepository.getByPollIds(pollIds)
        return watchers.groupBy { it.poll.id }
    }
}
