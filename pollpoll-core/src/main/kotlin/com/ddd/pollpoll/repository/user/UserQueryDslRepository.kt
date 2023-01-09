package com.ddd.pollpoll.repository.user

import com.querydsl.jpa.impl.JPAQueryFactory

interface UserQueryDslRepository

class UserQueryDslRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : UserQueryDslRepository
