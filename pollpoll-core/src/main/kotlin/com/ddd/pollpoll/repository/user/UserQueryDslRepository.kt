package com.ddd.pollpoll.repository.user

import com.ddd.pollpoll.domain.user.QUser.user
import com.ddd.pollpoll.domain.user.User
import com.querydsl.jpa.impl.JPAQueryFactory

interface UserQueryDslRepository {

    fun findAllTest(): List<User>
}

class UserQueryDslRepositoryImpl(private val jpaQueryFactory: JPAQueryFactory) : UserQueryDslRepository {

    override fun findAllTest(): List<User> {
        return jpaQueryFactory
            .selectFrom(user)
            .fetch()
    }
}
