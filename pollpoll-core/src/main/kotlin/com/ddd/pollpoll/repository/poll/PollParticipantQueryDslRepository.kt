package com.ddd.pollpoll.repository.poll

import com.ddd.pollpoll.domain.poll.PollParticipant
import com.ddd.pollpoll.domain.poll.QPollParticipant.pollParticipant
import com.querydsl.jpa.impl.JPAQueryFactory

interface PollParticipantQueryDslRepository {
    fun findByPollIds(pollIds: List<Long>): List<PollParticipant>
}

class PollParticipantQueryDslRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : PollParticipantQueryDslRepository {
    override fun findByPollIds(pollIds: List<Long>): List<PollParticipant> {
        return jpaQueryFactory
            .selectFrom(pollParticipant)
            .where(pollParticipant.poll.id.`in`(pollIds))
            .fetch()
    }
}
