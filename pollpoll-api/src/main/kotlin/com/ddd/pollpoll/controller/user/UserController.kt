package com.ddd.pollpoll.controller.user

import com.ddd.pollpoll.controller.user.dto.UpdateUserDto
import com.ddd.pollpoll.service.user.UserService
import com.ddd.pollpoll.util.getSocialId
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/users")
@RestController
class UserController(private val userService: UserService) {

    @PutMapping
    fun update(@RequestHeader("Authorization") bearerToken: String, @RequestBody dto: UpdateUserDto): Unit {
        val socialId = getSocialId(bearerToken)
        userService.updateNickname(socialId, dto)
    }
}
