package com.ddd.pollpoll.repository.post

import com.ddd.pollpoll.domain.post.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PostHitsRepository : JpaRepository<Post, Long>, PostQueryDslRepository {
    @Modifying
    @Query("update PostHits ph set ph.isDeleted = true where ph.post.id = :pollId")
    fun softDeleteByPostId(pollId: Long): Int
}
