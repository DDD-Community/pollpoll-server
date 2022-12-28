package com.ddd.pollpoll.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import java.util.*
import java.util.function.Function

private const val SECRET_KEY = "s1e2c3r4e5t6"

fun createJwt(socialId: String): String {
    val claims = HashMap<String, Any>()
    return createJwt(claims, socialId)
}

fun extractJwtSubject(token: String): String {
    return extractJwtClaim(token) { obj: Claims -> obj.subject }
}

private fun createJwt(claims: Map<String, Any>, subject: String): String {
    val currentTimeMillis = System.currentTimeMillis()
    return Jwts.builder()
        .setClaims(claims) // 주의: setSubject 이후 setClaims 할 경우 subject(sub)가 사라지는 현상이 있음
        .setSubject(subject)
        .setIssuedAt(Date(currentTimeMillis))
        .setExpiration(Date(currentTimeMillis + 1000 * 60 * 30)) // 30분
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact()
}

private fun <T> extractJwtClaim(token: String, claimsResolver: Function<Claims, T>): T {
    val claims = extractJwtClaims(token)
    return claimsResolver.apply(claims)
}

private fun extractJwtClaims(token: String): Claims {
    try {
        return Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(token)
            .body
    } catch (e: SecurityException) {
        throw RuntimeException("Invalid JWT signature")
    } catch (e: MalformedJwtException) {
        throw RuntimeException("Invalid JWT token")
    } catch (e: ExpiredJwtException) {
        throw RuntimeException("Expired JWT token")
    } catch (e: UnsupportedJwtException) {
        throw RuntimeException("Unsupported JWT token")
    } catch (e: IllegalArgumentException) {
        throw RuntimeException("JWT token compact of handler are invalid")
    }
}
