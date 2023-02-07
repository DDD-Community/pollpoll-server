package com.ddd.pollpoll.repository.poll

import com.ddd.pollpoll.domain.poll.QPoll.poll
import com.ddd.pollpoll.domain.poll.QPollItem.pollItem
import com.ddd.pollpoll.domain.post.QPost.post
import com.querydsl.core.annotations.QueryProjection
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDateTime

interface PollQueryDslRepository {
    fun getOneByPostId(postId: Long): PollDto?
    fun getListByPostIds(postIds: List<Long>): List<PollDto>
    fun countPollByUserId(userId: Long): Int
}

class PollQueryDslRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : PollQueryDslRepository {
    override fun getOneByPostId(postId: Long): PollDto? {
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

    override fun getListByPostIds(postIds: List<Long>): List<PollDto> {
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

    override fun countPollByUserId(userId: Long): Int {
        return jpaQueryFactory
            .selectFrom(poll)
            .innerJoin(poll.post, post)
            .where(post.user.id.eq(userId))
            .fetch().size
    }
}

data class PollDto @QueryProjection constructor(
    val postId: Long,
    val pollId: Long,
    val pollEndAt: LocalDateTime,
    val pollItemCount: Int,
)
