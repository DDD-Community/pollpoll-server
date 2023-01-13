package com.ddd.pollpoll.domain.post

import com.ddd.pollpoll.domain.common.BaseEntity
import org.hibernate.annotations.Where
import javax.persistence.Entity
import javax.persistence.Table

@Where(clause = "is_deleted = 0")
@Table(name = "category")
@Entity
class Category(
    val name: String,
    val imageUrl: String,
    val sequence: Int,
) : BaseEntity()
