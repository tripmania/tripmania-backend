package com.chichkanov.backend.user

import com.chichkanov.backend.error.CustomException
import com.chichkanov.backend.security.JwtTokenProvider
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus

@Service
class UserService constructor(
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder,
        private val jwtTokenProvider: JwtTokenProvider,
        private val authenticationManager: AuthenticationManager
) {

    fun signUp(userSignUpRequest: UserSignUpRequest): TokenResponse {
        if (userRepository.existsByLogin(userSignUpRequest.login)) {
            throw CustomException(HttpStatus.UNPROCESSABLE_ENTITY, "Login already in use")
        }
        if (userRepository.existsByEmail(userSignUpRequest.email)) {
            throw CustomException(HttpStatus.UNPROCESSABLE_ENTITY, "Email already in use")
        }

        val user = User(
                userSignUpRequest.email,
                userSignUpRequest.login,
                passwordEncoder.encode(userSignUpRequest.password),
                userSignUpRequest.name
        )
        userRepository.save(user)

        return TokenResponse(jwtTokenProvider.createToken(user.login, user.roles.toList()))
    }

    fun signIn(userSignInRequest: UserSignInRequest): TokenResponse {
        try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(
                    userSignInRequest.login,
                    userSignInRequest.password
            ))
            val user = userRepository.findByLogin(userSignInRequest.login)!!
            return TokenResponse(jwtTokenProvider.createToken(userSignInRequest.login, user.roles.toList()))
        } catch (e: AuthenticationException) {
            throw CustomException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid username/password supplied")
        }
    }

    fun refreshToken(login: String): TokenResponse {
        return TokenResponse(jwtTokenProvider.createToken(
                login,
                userRepository.findByLogin(login)!!.roles.toList()
        ))
    }

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun getUserById(id: Long): User? {
        return userRepository.findById(id).orElse(null)
    }

}

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User not found")
class UserNotFoundException : RuntimeException()