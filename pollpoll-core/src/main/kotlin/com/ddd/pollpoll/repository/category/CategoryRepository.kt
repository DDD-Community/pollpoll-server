package com.ddd.pollpoll.repository.category

import com.ddd.pollpoll.domain.post.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, Long>
