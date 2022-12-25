package com.ddd.pollpoll.controller.user

import com.ddd.pollpoll.domain.user.User
import com.ddd.pollpoll.service.user.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/users")
@RestController
class UserController(private val userService: UserService) {

    @GetMapping
    fun getAll(): List<User> {
        return userService.getAll()
    }
}
