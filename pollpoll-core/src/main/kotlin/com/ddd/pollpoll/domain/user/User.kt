package com.ddd.pollpoll.domain.user

import com.ddd.pollpoll.domain.common.BaseEntity
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table

@Table(name = "users")
@Entity
class User(
    @Enumerated(value = EnumType.STRING)
    val socialType: SocialType,
    val socialId: String,
    val email: String,
) : BaseEntity()
