package com.ddd.pollpoll.domain.poll

import com.ddd.pollpoll.domain.common.BaseEntity
import com.ddd.pollpoll.domain.post.Post
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Table(name = "poll")
@Entity
class Poll(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    val post: Post,
    val isMultipleChoice: Boolean,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
) : BaseEntity() {
    companion object {
        fun of(
            post: Post,
            isMultipleChoice: Boolean,
            milliseconds: Long
        ): Poll {
            val startAt = LocalDateTime.now()
            val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
            return Poll(
                post = post,
                isMultipleChoice = isMultipleChoice,
                startAt = startAt,
                endAt = startAt.plusMinutes(minutes)
            )
        }
    }
}
