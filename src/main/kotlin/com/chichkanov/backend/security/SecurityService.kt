package com.chichkanov.backend.security

import com.chichkanov.backend.user.User
import com.chichkanov.backend.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SecurityService @Autowired constructor(
        private val userRepository: UserRepository
) {

    fun signUp(securityUser: SecurityUser) {
        require(securityUser.email != null) { "Empty email" }
        require(securityUser.login != null) { "Empty login" }
        require(securityUser.password.isNotBlank()) { "Empty password" }
        require(userRepository.findByEmail(securityUser.email) == null) { "User with email ${securityUser.email} already exist" }
        require(userRepository.findByLogin(securityUser.login) == null) { "User with login ${securityUser.login} already exist" }

        val newUser = User(
                email = securityUser.email,
                login = securityUser.login,
                password = securityUser.password
        )

        userRepository.save(newUser)
    }

    fun signIn(securityUser: SecurityUser) {
        require(securityUser.login != null) { "Empty login" }
        require(securityUser.password.isNotBlank()) { "Empty password" }

        val user = userRepository.findByLogin(securityUser.login)
        require(user != null) { "No user found with login ${securityUser.login}" }
        require(securityUser.password == user.password) { "Incorrect password" }
    }

}