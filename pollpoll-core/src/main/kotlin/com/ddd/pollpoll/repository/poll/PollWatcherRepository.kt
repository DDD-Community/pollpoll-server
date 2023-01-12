package com.ddd.pollpoll.repository.poll

import com.ddd.pollpoll.domain.poll.PollWatcher
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PollWatcherRepository : JpaRepository<PollWatcher, Long>, PollWatcherQueryDslRepository {
    fun countByPoll_Id(pollId: Long): Int
}
