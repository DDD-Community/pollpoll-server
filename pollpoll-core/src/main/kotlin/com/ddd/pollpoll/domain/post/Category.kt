package com.ddd.pollpoll.domain.post

import com.ddd.pollpoll.domain.common.BaseEntity
import javax.persistence.Entity
import javax.persistence.Table

@Table(name = "category")
@Entity
class Category(
    val name: String,
    val imageUrl: String,
    val order: Int,
) : BaseEntity()
