package com.ddd.pollpoll.domain.post

import com.ddd.pollpoll.domain.common.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Table(name = "post")
@Entity
class Post(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    val category: Category,
    val title: String,
    @Column(columnDefinition = "TEXT")
    val contents: String
) : BaseEntity()
