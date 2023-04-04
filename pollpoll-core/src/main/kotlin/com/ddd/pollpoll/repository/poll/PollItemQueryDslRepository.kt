package com.ddd.pollpoll.repository.poll

import com.ddd.pollpoll.domain.poll.QPollItem.pollItem
import com.ddd.pollpoll.domain.poll.QPollParticipant.pollParticipant
import com.querydsl.core.annotations.QueryProjection
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory

interface PollItemQueryDslRepository {
    fun findByPollId(pollId: Long): List<PollItemDto>
    fun findBySocialIdAndPollId(userId: Long, pollId: Long): List<PollItemDto>
}

class PollItemQueryDslRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : PollItemQueryDslRepository {
    override fun findByPollId(pollId: Long): List<PollItemDto> {
        return jpaQueryFactory
            .select(
                QPollItemDto(pollItem.id, pollItem.name, Expressions.asBoolean(false))
            )
            .from(pollItem)
            .where(pollItem.poll.id.eq(pollId))
            .fetch()
    }

    override fun findBySocialIdAndPollId(userId: Long, pollId: Long): List<PollItemDto> {
        return jpaQueryFactory
            .select(
                QPollItemDto(pollItem.id, pollItem.name, pollParticipant.isNotNull)
            )
            .from(pollItem)
            .leftJoin(pollParticipant)
            .on(
                pollParticipant.pollItem.id.eq(pollItem.id)
                    .and(pollParticipant.isDeleted.isFalse)
                    .and(pollParticipant.user.id.eq(userId))
            )
            .where(pollItem.poll.id.eq(pollId))
            .fetch()
    }
}

data class PollItemDto @QueryProjection constructor(
    val pollItemId: Long,
    val name: String,
    val isPolled: Boolean
)
