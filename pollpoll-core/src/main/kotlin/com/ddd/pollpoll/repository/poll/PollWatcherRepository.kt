package com.ddd.pollpoll.repository.poll

import com.ddd.pollpoll.domain.poll.PollWatcher
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PollWatcherRepository : JpaRepository<PollWatcher, Long>, PollWatcherQueryDslRepository {
    fun countByPollId(pollId: Long): Int

    @Modifying
    @Query("update PollWatcher pw set pw.isDeleted = true where pw.poll.id = :pollId")
    fun softDeleteByPollId(pollId: Long): Int
}
