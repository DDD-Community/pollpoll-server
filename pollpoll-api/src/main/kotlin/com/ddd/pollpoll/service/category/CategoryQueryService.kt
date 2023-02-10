package com.ddd.pollpoll.service.category

import com.ddd.pollpoll.controller.category.dto.Categories
import com.ddd.pollpoll.domain.post.Category
import com.ddd.pollpoll.exception.ErrorCode.NOT_FOUND_CATEGORY
import com.ddd.pollpoll.exception.PollpollException
import com.ddd.pollpoll.repository.category.CategoryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class CategoryQueryService(
    private val categoryRepository: CategoryRepository
) {
    fun getCategory(categoryId: Long): Category {
        return categoryRepository.findByIdOrNull(categoryId)
            ?: throw PollpollException(NOT_FOUND_CATEGORY)
    }

    fun getCategories(): Categories {
        val categories = categoryRepository.findAll()
        return Categories(categories)
    }
}
