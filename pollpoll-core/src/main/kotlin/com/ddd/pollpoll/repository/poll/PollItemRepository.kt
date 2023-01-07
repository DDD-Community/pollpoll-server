package com.ddd.pollpoll.repository.poll

import com.ddd.pollpoll.domain.poll.PollItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PollItemRepository : JpaRepository<PollItem, Long>
