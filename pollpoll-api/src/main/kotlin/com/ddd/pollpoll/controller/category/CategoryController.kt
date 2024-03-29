package com.ddd.pollpoll.controller.category

import com.ddd.pollpoll.controller.SuccessResponse
import com.ddd.pollpoll.controller.category.dto.CategoryResponses
import com.ddd.pollpoll.service.category.CategoryQueryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "카테고리")
@RequestMapping("/api/categories")
@RestController
class CategoryController(
    private val categoryQueryService: CategoryQueryService
) {
    @Operation(summary = "카테고리 목록")
    @GetMapping
    fun getCategories(@RequestParam includeDefaultCategory: Boolean = false): SuccessResponse<CategoryResponses> {
        val categories = categoryQueryService.getCategories(includeDefaultCategory)
        return SuccessResponse(categories.toResponse())
    }
}
