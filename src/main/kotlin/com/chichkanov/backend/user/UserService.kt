package com.chichkanov.backend.user

import com.chichkanov.backend.error.CustomException
import com.chichkanov.backend.security.JwtTokenProvider
import com.chichkanov.backend.user.error.UserNotFoundException
import com.chichkanov.backend.user.model.*
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService constructor(
        private val userRepository: UserRepository,
        private val refreshTokenHashRepository: RefreshTokenHashRepository,
        private val passwordEncoder: PasswordEncoder,
        private val jwtTokenProvider: JwtTokenProvider,
        private val authenticationManager: AuthenticationManager
) {

    fun signUp(signUpRequest: SignUpRequest): TokenResponse {
        if (userRepository.existsByLogin(signUpRequest.login)) {
            throw CustomException(HttpStatus.UNPROCESSABLE_ENTITY, "Login already in use")
        }
        if (userRepository.existsByEmail(signUpRequest.email)) {
            throw CustomException(HttpStatus.UNPROCESSABLE_ENTITY, "Email already in use")
        }

        val user = User(
                signUpRequest.email,
                signUpRequest.login,
                passwordEncoder.encode(signUpRequest.password),
                signUpRequest.name
        )
        userRepository.save(user)
        return createTokenResponse(user)
    }

    fun signIn(signInRequest: SignInRequest): TokenResponse {
        try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(
                    signInRequest.login,
                    signInRequest.password
            ))
            val user = userRepository.findByLogin(signInRequest.login)!!
            return createTokenResponse(user)
        } catch (e: AuthenticationException) {
            throw CustomException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid username/password supplied")
        }
    }

    fun refreshToken(refreshTokenRequest: RefreshTokenRequest): TokenResponse {
        val oldRefreshToken = refreshTokenRequest.refreshToken
        jwtTokenProvider.validateToken(oldRefreshToken)
        val login = jwtTokenProvider.getLogin(oldRefreshToken)

        val user = userRepository.findByLogin(login)!!
        val newAccess = jwtTokenProvider.createAccessToken(user.login, user.roles.toList())
        val newRefresh = jwtTokenProvider.createRefreshToken(user.login, user.roles.toList())
        val newRefreshHash = newRefresh.hashCode()

        val currentRefreshes = refreshTokenHashRepository.findByUserId(user.id)
        refreshTokenHashRepository.save(RefreshTokenHash(user, newRefreshHash))

        val oldRefreshTokenModel = currentRefreshes.find { it.tokenHash == oldRefreshToken.hashCode() }
        if (oldRefreshTokenModel != null) {
            refreshTokenHashRepository.delete(oldRefreshTokenModel)
        }

        return TokenResponse(newAccess, newRefresh)
    }

    fun getUserById(id: Long): User {
        return userRepository.findById(id).orElseThrow { throw UserNotFoundException() }
    }

    fun gerUserByLogin(userLogin: String): User {
        return userRepository.findByLogin(userLogin) ?: throw UserNotFoundException()
    }

    fun updateUserInfo(userLogin: String, updateUserInfoRequest: UpdateUserInfoReauest): User {
        val user = userRepository.findByLogin(userLogin) ?: throw UserNotFoundException()
        user.status = updateUserInfoRequest.status
        user.name = updateUserInfoRequest.name
        user.photoUrl = updateUserInfoRequest.photoUrl
        return userRepository.save(user)
    }

    private fun createTokenResponse(user: User): TokenResponse {
        val newAccess = jwtTokenProvider.createAccessToken(user.login, user.roles.toList())
        val newRefresh = jwtTokenProvider.createRefreshToken(user.login, user.roles.toList())
        return TokenResponse(newAccess, newRefresh)
    }

}