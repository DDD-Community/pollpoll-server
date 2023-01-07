package com.ddd.pollpoll.repository.poll

import com.ddd.pollpoll.domain.poll.Poll
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PollRepository : JpaRepository<Poll, Long>
