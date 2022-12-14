package com.ddd.pollpoll.service.category

import com.ddd.pollpoll.controller.category.dto.Categories
import com.ddd.pollpoll.domain.post.Category
import com.ddd.pollpoll.repository.category.CategoryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {
    fun getCategory(categoryId: Long): Category {
        return categoryRepository.findByIdOrNull(categoryId)
            ?: throw RuntimeException("존재하지 않는 카테고리입니다. (categoryId: ${categoryId})")
    }

    fun getCategories(): Categories {
        val categories = categoryRepository.findAll()
        return Categories(categories)
    }
}
