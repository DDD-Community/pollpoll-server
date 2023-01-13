package com.ddd.pollpoll.domain.post

import com.ddd.pollpoll.domain.common.BaseEntity
import com.ddd.pollpoll.domain.user.User
import org.hibernate.annotations.Where
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import javax.persistence.Table

@Where(clause = "is_deleted = 0")
@Table(name = "post")
@Entity
class Post(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    val category: Category,
    val title: String,
    @Column(columnDefinition = "TEXT")
    val contents: String,
) : BaseEntity()
