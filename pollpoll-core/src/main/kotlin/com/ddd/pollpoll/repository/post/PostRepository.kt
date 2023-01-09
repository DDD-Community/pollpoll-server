package com.ddd.pollpoll.repository.post

import com.ddd.pollpoll.domain.post.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<Post, Long>, PostQueryDslRepository
