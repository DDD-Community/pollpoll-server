package com.ddd.pollpoll.repository.user

import com.ddd.pollpoll.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long>, UserQueryDslRepository {

    fun findBySocialId(socialId: String): User?
}
