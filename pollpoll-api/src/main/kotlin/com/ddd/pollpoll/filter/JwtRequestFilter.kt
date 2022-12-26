package com.ddd.pollpoll.filter

import com.ddd.pollpoll.util.JwtUtil
import org.apache.commons.lang3.StringUtils
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtRequestFilter : OncePerRequestFilter() {

    companion object {
        const val TOKEN_PREFIX: String = "Bearer "
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            val accessToken = authorizationHeader.substring(TOKEN_PREFIX.length)
            val subject = JwtUtil.extractSubject(accessToken)
            val authorities = listOf(SimpleGrantedAuthority("USER"))
            val principal = User(subject, StringUtils.EMPTY, authorities)

            val authentication = UsernamePasswordAuthenticationToken(principal, accessToken, authorities)
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }
}
