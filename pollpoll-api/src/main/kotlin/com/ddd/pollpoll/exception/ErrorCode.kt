package com.ddd.pollpoll.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val code: String,
    val message: String,
) {
    // [01xx] USER
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "0101", "존재하지 않는 사용자입니다."),

    // [02xx] CATEGORY
    NOT_FOUND_CATEGORY(HttpStatus.BAD_REQUEST, "0201", "존재하지 않는 카테고리입니다."),

    // [03xx] POST
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "0301", "존재하지 않는 게시글입니다."),
    FORBIDDEN_TO_DELETE_POST(HttpStatus.FORBIDDEN, "0302", "본인의 게시글만 삭제할 수 있습니다."),

    // [04xx] POLL
    NOT_FOUND_POLL(HttpStatus.BAD_REQUEST, "0401", "존재하지 않는 투표입니다."),
    MINIMUM_POLL_ITEM_COUNT(HttpStatus.BAD_REQUEST, "0402", "투표 항목은 2개 이상이어야 합니다."),
    INVALID_POLL_ITEM(HttpStatus.BAD_REQUEST, "0403", "유효하지 않은 투표 항목입니다."),
}
