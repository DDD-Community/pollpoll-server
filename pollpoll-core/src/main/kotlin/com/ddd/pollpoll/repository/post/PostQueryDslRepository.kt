package com.ddd.pollpoll.repository.post

import com.ddd.pollpoll.domain.post.QCategory.category
import com.ddd.pollpoll.domain.post.QPost.post
import com.ddd.pollpoll.domain.post.QPostHits.postHits
import com.ddd.pollpoll.domain.user.QUser.user
import com.querydsl.core.annotations.QueryProjection
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDateTime

interface PostQueryDslRepository {
    fun getPostsByLastPostId(postId: Long): List<PostDto>
    fun getPostById(postId: Long): PostDto?
}

class PostQueryDslRepositoryImpl(private val jpaQueryFactory: JPAQueryFactory) : PostQueryDslRepository {

    override fun getPostsByLastPostId(postId: Long): List<PostDto> {
        return jpaQueryFactory
            .select(
                QPostDto(
                    post.id,
                    post.title,
                    post.contents,
                    post.createdAt,
                    postHits.count().intValue(),
                    user.nickname,
                    category.name
                )
            )
            .from(post)
            .innerJoin(post.user, user)
            .innerJoin(post.category, category)
            .leftJoin(postHits).on(postHits.post.id.eq(post.id))
            .where(post.id.lt(postId))
            .orderBy(post.id.desc())
            .limit(2)
            .fetch()
    }

    override fun getPostById(postId: Long): PostDto? {
        return jpaQueryFactory
            .select(
                QPostDto(
                    post.id,
                    post.title,
                    post.contents,
                    post.createdAt,
                    postHits.count().intValue(),
                    user.nickname,
                    category.name
                )
            )
            .from(post)
            .innerJoin(post.user, user)
            .innerJoin(post.category, category)
            .leftJoin(postHits).on(postHits.post.id.eq(postId))
            .where(post.id.eq(postId))
            .groupBy(post.id)
            .fetchOne()
    }
}

data class PostDto @QueryProjection constructor(
    val postId: Long,
    val postTitle: String,
    val postContents: String,
    val postCreatedAt: LocalDateTime,
    val postHits: Int,
    val nickname: String,
    val categoryName: String,
)
