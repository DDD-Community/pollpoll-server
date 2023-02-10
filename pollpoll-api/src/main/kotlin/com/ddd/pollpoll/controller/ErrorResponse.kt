package com.ddd.pollpoll.controller

import com.ddd.pollpoll.exception.ErrorCode

fun errorResponseOf(errorCode: ErrorCode): ErrorResponse {
    return ErrorResponse(errorCode.code, errorCode.message)
}

data class ErrorResponse(val code: String, val message: String)
