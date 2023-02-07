package com.ddd.pollpoll.repository.poll

import com.ddd.pollpoll.domain.poll.PollWatcher
import com.ddd.pollpoll.domain.poll.QPollWatcher.pollWatcher
import com.querydsl.jpa.impl.JPAQueryFactory

interface PollWatcherQueryDslRepository {
    fun getListByPollIds(pollIds: List<Long>): List<PollWatcher>

    fun countWatchPollByUserId(userId: Long): Int
}

class PollWatcherQueryDslRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : PollWatcherQueryDslRepository {
    override fun getListByPollIds(pollIds: List<Long>): List<PollWatcher> {
        return jpaQueryFactory
            .selectFrom(pollWatcher)
            .where(pollWatcher.poll.id.`in`(pollIds))
            .fetch()
    }

    override fun countWatchPollByUserId(userId: Long): Int {
        return jpaQueryFactory
            .selectFrom(pollWatcher)
            .where(pollWatcher.user.id.eq(userId))
            .fetch().size
    }
}
