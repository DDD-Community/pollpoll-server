package com.ddd.pollpoll.service.category

import com.ddd.pollpoll.controller.category.dto.Categories
import com.ddd.pollpoll.controller.category.dto.CategoryResponses
import com.ddd.pollpoll.repository.category.CategoryRepository
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {
    fun getCategories(): CategoryResponses {
        val list = categoryRepository.findAll()
        val categories = Categories(list)
        return categories.toResponse()
    }
}
