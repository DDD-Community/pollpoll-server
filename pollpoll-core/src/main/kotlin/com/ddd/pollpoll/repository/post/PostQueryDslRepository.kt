package com.ddd.pollpoll.repository.post

import com.ddd.pollpoll.domain.post.QCategory.category
import com.ddd.pollpoll.domain.post.QPost.post
import com.ddd.pollpoll.domain.post.QPostHits.postHits
import com.ddd.pollpoll.domain.user.QUser.user
import com.querydsl.core.annotations.QueryProjection
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDateTime

interface PostQueryDslRepository {
    fun getOneById(postId: Long): PostDto?
    fun getListByLastPostIdAndKeyword(postId: Long, keyword: String?): List<PostDto>
}

class PostQueryDslRepositoryImpl(private val jpaQueryFactory: JPAQueryFactory) : PostQueryDslRepository {

    override fun getOneById(postId: Long): PostDto? {
        return commonQuery()
            .leftJoin(postHits).on(postHits.post.id.eq(postId))
            .where(post.id.eq(postId))
            .groupBy(post.id)
            .fetchOne()
    }

    override fun getListByLastPostIdAndKeyword(postId: Long, keyword: String?): List<PostDto> {
        return commonQuery()
            .leftJoin(postHits).on(postHits.post.id.eq(post.id))
            .where(
                post.id.lt(postId),
                if (keyword === null) null else post.title.contains(keyword), // todo: refactoring
            )
            .groupBy(post.id)
            .orderBy(post.id.desc())
            .limit(2)
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
