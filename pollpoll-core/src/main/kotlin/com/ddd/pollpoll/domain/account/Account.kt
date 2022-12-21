package com.ddd.pollpoll.domain.account

import com.ddd.pollpoll.domain.common.BaseEntity
import javax.persistence.Entity

@Entity
internal class Account(
    val name: String
) : BaseEntity()
