package com.ddd.pollpoll.domain.post

import com.ddd.pollpoll.domain.common.BaseEntity
import com.ddd.pollpoll.domain.user.User
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Table(name = "post_hits")
@Entity
class PostHits(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    val post: Post
) : BaseEntity()
