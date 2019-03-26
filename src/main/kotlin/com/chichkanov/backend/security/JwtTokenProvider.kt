package com.chichkanov.backend.security

import com.chichkanov.backend.user.model.Role
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest

@Component
class JwtTokenProvider constructor(
        private val myUserDetails: MyUserDetails
) {

    /**
     * THIS IS NOT A SECURE PRACTICE! For simplicity, we are storing a static key here. Ideally, in a
     * microservices environment, this key would be kept on a config-server.
     */
    @Value("\${security.jwt.token.secret-key:secret-key}")
    private lateinit var secretKey: String

    @Value("\${security.jwt.token.expire-length}")
    private val validityInMilliseconds: Long = 3600000

    @PostConstruct
    protected fun init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.toByteArray())
    }

    fun createToken(login: String, roles: List<Role>): String {
        val claims = Jwts.claims().setSubject(login)
        claims["auth"] = roles.map { SimpleGrantedAuthority(it.authority) }

        val now = Date()
        val validity = Date(now.time + validityInMilliseconds)

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val userDetails = myUserDetails.loadUserByUsername(getLogin(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun getLogin(token: String): String {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
    }

    fun getLogin(req: HttpServletRequest): String? {
        val token = resolveToken(req) ?: return null
        return getLogin(token)
    }

    fun resolveToken(req: HttpServletRequest): String? {
        val bearerToken = req.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }

    fun validateToken(token: String): Boolean {
        // TODO Return 401 error code in case of token expired
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
            return true
        } catch (e: JwtException) {
            throw RuntimeException("Expired or invalid JWT token")
        } catch (e: IllegalArgumentException) {
            throw RuntimeException("Expired or invalid JWT token")
        }
    }

}