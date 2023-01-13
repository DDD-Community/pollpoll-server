package com.ddd.pollpoll.controller.post

import com.ddd.pollpoll.controller.SuccessResponse
import com.ddd.pollpoll.controller.post.dto.CreatePostRequest
import com.ddd.pollpoll.controller.post.dto.PostPollResponse
import com.ddd.pollpoll.controller.post.dto.PostPollResponses
import com.ddd.pollpoll.service.post.PostCommandService
import com.ddd.pollpoll.service.post.PostQueryService
import com.ddd.pollpoll.util.getSocialId
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "게시글")
@RequestMapping("/api/posts")
@RestController
class PostController(
    private val postCommandService: PostCommandService,
    private val postQueryService: PostQueryService
) {
    @Operation(summary = "게시글 등록")
    @PostMapping
    fun createPost(
        @RequestHeader("Authorization") bearerToken: String,
        @RequestBody dto: CreatePostRequest,
    ) {
        val socialId = getSocialId(bearerToken)
        postCommandService.create(socialId, dto)
    }

    @Operation(summary = "게시글 목록 조회")
    @GetMapping
    fun getPosts(@RequestParam lastPostId: Long): SuccessResponse<PostPollResponses> {
        return SuccessResponse(postQueryService.getShowMorePosts(lastPostId))
    }

    @Operation(summary = "게시글 단건 조회")
    @GetMapping("/{postId}")
    fun getPost(@PathVariable postId: Long): SuccessResponse<PostPollResponse> {
        return SuccessResponse(postQueryService.getPost(postId))
    }
}
