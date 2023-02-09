package com.ddd.pollpoll.repository.poll

import com.ddd.pollpoll.domain.poll.PollItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PollItemRepository : JpaRepository<PollItem, Long> {
    @Modifying
    @Query("update PollItem pi set pi.isDeleted = true where pi.poll.id = :pollId")
    fun softDeleteByPollId(pollId: Long): Int

    fun findByPollId(pollId: Long): List<PollItem>
}
