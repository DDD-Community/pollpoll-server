package com.ddd.pollpoll.controller.post

import com.ddd.pollpoll.controller.post.dto.CreatePostRequest
import com.ddd.pollpoll.service.post.PostService
import com.ddd.pollpoll.util.getSocialId
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "게시글")
@RequestMapping("/api/post")
@RestController
class PostController(
    private val postService: PostService
) {
    @Operation(summary = "게시글 등록")
    @PostMapping
    fun createPost(
        @RequestHeader("Authorization") bearerToken: String,
        @RequestBody dto: CreatePostRequest,
    ) {
        val socialId = getSocialId(bearerToken)
        postService.create(socialId, dto)
    }
}
