package com.ddd.pollpoll.service.user

import com.ddd.pollpoll.domain.user.User
import com.ddd.pollpoll.repository.user.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun getAll(): List<User> {
        return userRepository.findAllTest()
    }
}
