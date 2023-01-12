package com.ddd.pollpoll.repository.poll

import com.ddd.pollpoll.domain.poll.QPoll.poll
import com.ddd.pollpoll.domain.poll.QPollItem.pollItem
import com.querydsl.core.annotations.QueryProjection
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDateTime

interface PollQueryDslRepository {
    fun getPollByPostId(postId: Long): PollDto?
    fun getPollsByPostIds(postIds: List<Long>): List<PollDto>
}

class PollQueryDslRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : PollQueryDslRepository {
    override fun getPollByPostId(postId: Long): PollDto? {
        return jpaQueryFactory
            .select(
                QPollDto(
                    poll.post.id,
                    poll.id,
                    poll.endAt,
                    pollItem.count().intValue()
                )
            )
            .from(poll)
            .innerJoin(pollItem).on(pollItem.poll.id.eq(poll.id))
            .where(poll.post.id.eq(postId))
            .groupBy(poll.id)
            .fetchOne()
    }

    override fun getPollsByPostIds(postIds: List<Long>): List<PollDto> {
        return jpaQueryFactory
            .select(
                QPollDto(
                    poll.post.id,
                    poll.id,
                    poll.endAt,
                    pollItem.count().intValue()
                )
            )
            .from(poll)
            .innerJoin(pollItem).on(pollItem.poll.id.eq(poll.id))
            .where(poll.post.id.`in`(postIds))
            .groupBy(poll.id)
            .fetch()
    }
}

data class PollDto @QueryProjection constructor(
    val postId: Long,
    val pollId: Long,
    val pollEndAt: LocalDateTime,
    val pollItemCount: Int,
)
