package com.ddd.pollpoll.repository.poll

import com.ddd.pollpoll.domain.poll.PollParticipant
import com.ddd.pollpoll.domain.poll.QPollItem.pollItem
import com.ddd.pollpoll.domain.poll.QPollParticipant.pollParticipant
import com.querydsl.core.annotations.QueryProjection
import com.querydsl.jpa.impl.JPAQueryFactory

interface PollParticipantQueryDslRepository {
    fun getListByPollIds(pollIds: List<Long>): List<PollParticipant>

    fun countParticipatePollByUserId(userId: Long): Int

    fun getParticipateCountByPollId(pollId: Long): List<ParticipateCount>
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

    override fun getParticipateCountByPollId(pollId: Long): List<ParticipateCount> {
        return jpaQueryFactory
            .select(QParticipateCount(pollItem.id, pollParticipant.count()))
            .from(pollItem)
            .leftJoin(pollParticipant)
            .on(
                pollParticipant.pollItem.id.eq(pollItem.id)
                    .and(pollParticipant.isDeleted.isFalse)
            )
            .where(pollItem.poll.id.eq(pollId))
            .groupBy(pollItem.id)
            .fetch()
    }
}

data class ParticipateCount @QueryProjection constructor(
    val pollItemId: Long,
    val count: Long
)
