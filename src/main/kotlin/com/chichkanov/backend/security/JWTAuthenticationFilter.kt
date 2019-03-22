package com.chichkanov.backend.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm.HMAC512
import com.chichkanov.backend.security.SecurityConstants.EXPIRATION_TIME
import com.chichkanov.backend.security.SecurityConstants.HEADER_STRING
import com.chichkanov.backend.security.SecurityConstants.SECRET
import com.chichkanov.backend.security.SecurityConstants.TOKEN_PREFIX
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JWTAuthenticationFilter(
        private val authenticationManager: AuthenticationManager
) : UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        try {
            val authUser = ObjectMapper().readValue(request.inputStream, SecurityUser::class.java)
            return authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(
                            authUser.login,
                            authUser.password,
                            emptyList()
                    )
            )
        } catch (e: Exception) {
            throw RuntimeException("Error authenticate user")
        }
    }

    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authResult: Authentication) {
        val token = JWT.create()
                .withSubject((authResult.principal as User).username)
                .withExpiresAt(Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.toByteArray()))

        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token)
    }

    override fun getAuthenticationManager(): AuthenticationManager {
        return authenticationManager
    }

}