package com.ddd.pollpoll.repository.poll

import com.ddd.pollpoll.domain.poll.Poll
import com.ddd.pollpoll.domain.poll.PollWatcher
import com.ddd.pollpoll.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PollWatcherRepository : JpaRepository<PollWatcher, Long>, PollWatcherQueryDslRepository {
    fun countByPollId(pollId: Long): Int

    fun findByPollAndUser(poll: Poll, user: User): PollWatcher?

    @Modifying
    @Query("update PollWatcher pw set pw.isDeleted = true where pw.poll.id = :pollId")
    fun softDeleteByPollId(pollId: Long): Int
}
