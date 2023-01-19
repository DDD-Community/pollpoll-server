package com.ddd.pollpoll.repository.poll

import com.ddd.pollpoll.domain.poll.PollParticipant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PollParticipantRepository : JpaRepository<PollParticipant, Long>, PollParticipantQueryDslRepository {
    fun findByUserIdAndPollId(userId: Long, pollId: Long): List<PollParticipant>

    fun countByPollId(pollId: Long): Int

    @Modifying
    @Query("update PollParticipant pp set pp.isDeleted = true where pp.poll.id = :pollId")
    fun softDeleteByPollId(pollId: Long): Int
}
