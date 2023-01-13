package com.ddd.pollpoll.domain.poll

import com.ddd.pollpoll.domain.common.BaseEntity
import org.hibernate.annotations.Where
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Where(clause = "is_deleted = 0")
@Table(name = "poll_item")
@Entity
class PollItem(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id")
    val poll: Poll,
    val name: String,
) : BaseEntity()
