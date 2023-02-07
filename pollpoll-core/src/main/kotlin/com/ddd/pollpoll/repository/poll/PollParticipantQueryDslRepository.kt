package com.ddd.pollpoll.repository.poll

import com.ddd.pollpoll.domain.poll.PollParticipant
import com.ddd.pollpoll.domain.poll.QPollParticipant.pollParticipant
import com.querydsl.jpa.impl.JPAQueryFactory

interface PollParticipantQueryDslRepository {
    fun getListByPollIds(pollIds: List<Long>): List<PollParticipant>

    fun countParticipatePollByUserId(userId: Long): Int
}

class PollParticipantQueryDslRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : PollParticipantQueryDslRepository {
    override fun getListByPollIds(pollIds: List<Long>): List<PollParticipant> {
        return jpaQueryFactory
            .selectFrom(pollParticipant)
            .where(pollParticipant.poll.id.`in`(pollIds))
            .fetch()
    }

    override fun countParticipatePollByUserId(userId: Long): Int {
        return jpaQueryFactory
            .selectFrom(pollParticipant)
            .where(pollParticipant.user.id.eq(userId))
            .fetch().size
    }
}
