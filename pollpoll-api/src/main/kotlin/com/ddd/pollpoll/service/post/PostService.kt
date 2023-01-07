package com.ddd.pollpoll.service.post

import com.ddd.pollpoll.controller.post.dto.CreatePostRequest
import com.ddd.pollpoll.controller.post.dto.PollItemDto
import com.ddd.pollpoll.domain.poll.Poll
import com.ddd.pollpoll.domain.poll.PollItem
import com.ddd.pollpoll.domain.post.Post
import com.ddd.pollpoll.repository.poll.PollItemRepository
import com.ddd.pollpoll.repository.poll.PollRepository
import com.ddd.pollpoll.repository.post.PostRepository
import com.ddd.pollpoll.service.category.CategoryService
import org.springframework.stereotype.Service

@Service
class PostService(
    private val categoryService: CategoryService,
    private val postRepository: PostRepository,
    private val pollRepository: PollRepository,
    private val pollItemRepository: PollItemRepository,
) {
    fun create(socialId: String, dto: CreatePostRequest) {
        val category = categoryService.getCategory(dto.categoryId)
        val post = Post(category = category, title = dto.title, contents = dto.contents)

        val pollDto = dto.pollDto
        val poll = Poll.of(post = post, isMultipleChoice = pollDto.multipleChoice, milliseconds = pollDto.milliseconds)

        val pollItemDtos = pollDto.pollItemDtos
        val pollItems = getPollItems(pollItemDtos, poll)

        postRepository.save(post)
        pollRepository.save(poll)
        pollItemRepository.saveAll(pollItems)
    }

    private fun getPollItems(
        pollItemDtos: List<PollItemDto>,
        poll: Poll
    ): List<PollItem> {
        return pollItemDtos
            .map { it.toEntity(poll) }
            .takeIf { it.size >= 2 }
            ?: throw RuntimeException("투표 항목은 2개 이상이어야 합니다.")
    }
}
