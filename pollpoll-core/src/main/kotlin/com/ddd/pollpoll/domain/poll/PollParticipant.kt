package com.ddd.pollpoll.domain.poll

import com.ddd.pollpoll.domain.common.BaseEntity
import com.ddd.pollpoll.domain.user.User
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Table(name = "poll_participant")
@Entity
class PollParticipant(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id")
    val poll: Poll,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_item_id")
    val pollItem: PollItem
) : BaseEntity()
