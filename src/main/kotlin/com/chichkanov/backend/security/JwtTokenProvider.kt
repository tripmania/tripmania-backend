package com.chichkanov.backend.security

import com.chichkanov.backend.user.model.Role
import io.jsonwebtoken.ExpiredJwtException
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

    @Value("\${security.jwt.token.secret-key:secret-key}")
    private lateinit var secretKey: String

    @Value("\${security.jwt.token.access-expire-length}")
    private val accessValidityInMilliseconds: Long = 3600000

    @Value("\${security.jwt.token.refresh-expire-length}")
    private val refreshValidityInMilliseconds: Long = 86400000

    @PostConstruct
    protected fun init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.toByteArray())
    }

    fun createAccessToken(login: String, roles: List<Role>): String {
        return createAccessToken(login, roles, Date().time + accessValidityInMilliseconds)
    }

    fun createRefreshToken(login: String, roles: List<Role>): String {
        return createAccessToken(login, roles, Date().time + refreshValidityInMilliseconds)
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
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
            return true
        } catch (e: ExpiredJwtException) {
            throw RuntimeException("Expired JWT token")
        } catch (e: RuntimeException) {
            throw RuntimeException("Expired or invalid JWT token")
        }
    }

    private fun createAccessToken(login: String, roles: List<Role>, expiration: Long): String {
        val claims = Jwts.claims().setSubject(login)
        claims["auth"] = roles.map { SimpleGrantedAuthority(it.authority) }

        val now = Date()
        val validity = Date(expiration)

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact()
    }

}