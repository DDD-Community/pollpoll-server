package com.ddd.polllpoll.domain.user

import com.ddd.polllpoll.domain.common.BaseEntity
import javax.persistence.Entity

@Entity
internal class Account(
    val name: String
) : BaseEntity()
