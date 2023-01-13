package com.ddd.pollpoll.domain.user

import com.ddd.pollpoll.domain.common.BaseEntity
import org.hibernate.annotations.Where
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table

@Where(clause = "is_deleted = 0")
@Table(name = "users")
@Entity
class User(
    var nickname: String? = null,
    @Enumerated(value = EnumType.STRING)
    val socialType: SocialType,
    val socialId: String,
    val email: String,
) : BaseEntity()

enum class SocialType(val description: String) {
    GOOGLE("구글"),
}
