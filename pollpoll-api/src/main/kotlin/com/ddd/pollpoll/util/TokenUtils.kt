package com.ddd.pollpoll.util

const val TOKEN_PREFIX: String = "Bearer "

fun getSocialId(bearerToken: String): String {
    val token = bearerToken.substring(TOKEN_PREFIX.length)
    return extractJwtSubject(token)
}
