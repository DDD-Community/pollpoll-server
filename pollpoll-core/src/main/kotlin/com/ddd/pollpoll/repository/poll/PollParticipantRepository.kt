package com.ddd.pollpoll.repository.poll

import com.ddd.pollpoll.domain.poll.PollParticipant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PollParticipantRepository : JpaRepository<PollParticipant, Long>, PollParticipantQueryDslRepository {
    fun countByPoll_Id(pollId: Long): Int
}
