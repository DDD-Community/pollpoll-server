package com.ddd.pollpoll.domain.user

import com.ddd.pollpoll.domain.common.BaseEntity
import javax.persistence.Entity
import javax.persistence.Table

@Table(name = "users")
@Entity
class User(
    val name: String,
    val socialId: String
) : BaseEntity()
