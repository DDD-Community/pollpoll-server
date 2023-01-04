package com.ddd.pollpoll.domain.poll

import com.ddd.pollpoll.domain.common.BaseEntity
import com.ddd.pollpoll.domain.post.Post
import java.time.LocalDateTime
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
    @Enumerated(value = EnumType.STRING)
    val status: Status,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
) : BaseEntity()

enum class Status(val description: String) {
    IN_PROGRESS("진행중"),
    DONE("완료")
}
