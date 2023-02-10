package com.ddd.pollpoll.exception

import com.ddd.pollpoll.controller.ErrorResponse
import com.ddd.pollpoll.controller.errorResponseOf
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

class PollpollException(val errorCode: ErrorCode) : RuntimeException()

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [PollpollException::class])
    fun handlePollpollException(e: PollpollException): ResponseEntity<ErrorResponse> {
        val errorCode = e.errorCode
        return ResponseEntity(errorResponseOf(errorCode), errorCode.status)
    }
}
