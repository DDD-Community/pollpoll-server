package com.ddd.pollpoll.repository.post

import com.ddd.pollpoll.domain.poll.QPoll.poll
import com.ddd.pollpoll.domain.poll.QPollParticipant.pollParticipant
import com.ddd.pollpoll.domain.poll.QPollWatcher.pollWatcher
import com.ddd.pollpoll.domain.post.QCategory.category
import com.ddd.pollpoll.domain.post.QPost.post
import com.ddd.pollpoll.domain.post.QPostHits.postHits
import com.ddd.pollpoll.domain.user.QUser.user
import com.querydsl.core.annotations.QueryProjection
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDateTime

interface PostQueryDslRepository {
    fun getOneById(postId: Long): PostDto?
    fun getListByLastPostIdAndKeyword(lastPostId: Long, keyword: String?): List<PostDto>
    fun getMyPostsByLastPostIdAndUserId(lastPostId: Long, userId: Long): List<PostDto>
    fun getParticipatePostsByLastPostIdAndUserId(lastPostId: Long, userId: Long): List<PostDto>
    fun getWatchPostsByLastPostIdAndUser(lastPostId: Long, userId: Long): List<PostDto>
}

const val PAGE_LIMIT = 2L

class PostQueryDslRepositoryImpl(private val jpaQueryFactory: JPAQueryFactory) : PostQueryDslRepository {

    override fun getOneById(postId: Long): PostDto? {
        return commonQuery()
            .leftJoin(postHits).on(postHits.post.id.eq(postId))
            .where(post.id.eq(postId))
            .groupBy(post.id)
            .fetchOne()
    }

    override fun getListByLastPostIdAndKeyword(lastPostId: Long, keyword: String?): List<PostDto> {
        return commonQuery()
            .leftJoin(postHits).on(postHits.post.id.eq(post.id))
            .where(
                post.id.lt(lastPostId),
                if (keyword === null) null else post.title.contains(keyword), // todo: refactoring
            )
            .groupBy(post.id)
            .orderBy(post.id.desc())
            .limit(PAGE_LIMIT)
            .fetch()
    }

    override fun getMyPostsByLastPostIdAndUserId(lastPostId: Long, userId: Long): List<PostDto> {
        return commonQuery()
            .where(
                post.id.lt(lastPostId),
                post.user.id.eq(userId),
            )
            .groupBy(post.id)
            .orderBy(post.id.desc())
            .limit(PAGE_LIMIT)
            .fetch()
    }

    override fun getParticipatePostsByLastPostIdAndUserId(lastPostId: Long, userId: Long): List<PostDto> {
        return commonQuery()
            .innerJoin(poll).on(poll.post.id.eq(post.id))
            .innerJoin(pollParticipant).on(pollParticipant.poll.id.eq(poll.id))
            .where(
                post.id.lt(lastPostId),
                pollParticipant.user.id.eq(userId),
                pollParticipant.isDeleted.isFalse
            )
            .groupBy(post.id)
            .orderBy(post.id.desc())
            .limit(PAGE_LIMIT)
            .fetch()
    }

    override fun getWatchPostsByLastPostIdAndUser(lastPostId: Long, userId: Long): List<PostDto> {
        return commonQuery()
            .innerJoin(poll).on(poll.post.id.eq(post.id))
            .innerJoin(pollWatcher).on(pollWatcher.poll.id.eq(poll.id))
            .where(
                post.id.lt(lastPostId),
                pollWatcher.user.id.eq(userId),
                pollWatcher.isDeleted.isFalse
            )
            .groupBy(post.id)
            .orderBy(post.id.desc())
            .limit(PAGE_LIMIT)
            .fetch()
    }

    private fun commonQuery() = jpaQueryFactory
        .select(
            QPostDto(
                post.id,
                post.title,
                post.contents,
                post.createdAt,
                postHits.count().intValue(),
                user.id,
                user.nickname,
                category.name,
            )
        )
        .from(post)
        .innerJoin(post.user, user)
        .innerJoin(post.category, category)
        .leftJoin(postHits).on(postHits.post.id.eq(post.id))
}

data class PostDto @QueryProjection constructor(
    val postId: Long,
    val postTitle: String,
    val postContents: String,
    val postCreatedAt: LocalDateTime,
    val postHits: Int,
    val userId: Long,
    val nickname: String,
    val categoryName: String,
)
