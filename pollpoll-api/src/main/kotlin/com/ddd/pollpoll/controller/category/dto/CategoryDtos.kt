package com.ddd.pollpoll.controller.category.dto

import com.ddd.pollpoll.domain.post.Category

data class CategoryResponses(
    val categories: List<CategoryResponse>,
)

data class CategoryResponse(
    val categoryId: Long,
    val name: String,
    val imageUrl: String,
)

class Categories(
    private val categories: List<Category>
) {
    fun toResponse(): CategoryResponses {
        val list = categories.map { CategoryResponse(it.id, it.name, it.imageUrl) }
        return CategoryResponses(list)
    }
}
