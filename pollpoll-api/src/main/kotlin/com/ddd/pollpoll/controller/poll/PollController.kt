package com.ddd.pollpoll.controller.poll

import com.ddd.pollpoll.controller.poll.dto.ParticipantRequest
import com.ddd.pollpoll.service.poll.PollCommandService
import com.ddd.pollpoll.util.getSocialId
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "투표")
@RequestMapping("/api/polls")
@RestController
class PollController(
    private val pollCommandService: PollCommandService,
) {
    @Operation(summary = "투표하기, 다시 투표하기")
    @PutMapping("/{pollId}/participate")
    fun participant(
        @RequestHeader("Authorization") bearerToken: String,
        @PathVariable pollId: Long,
        @RequestBody dto: ParticipantRequest,
    ) {
        val socialId = getSocialId(bearerToken)
        pollCommandService.participant(socialId, pollId, dto)
    }

    @Operation(summary = "구경하기")
    @PostMapping("/{pollId}/watch")
    fun watch(
        @RequestHeader("Authorization") bearerToken: String,
        @PathVariable pollId: Long,
    ) {
        val socialId = getSocialId(bearerToken)
        pollCommandService.watch(socialId, pollId)
    }
}
