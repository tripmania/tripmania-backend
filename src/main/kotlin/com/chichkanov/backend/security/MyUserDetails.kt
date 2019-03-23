package com.chichkanov.backend.security

import com.chichkanov.backend.user.UserRepository
import com.chichkanov.backend.user.error.UserNotFoundException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class MyUserDetails constructor(
        private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByLogin(username) ?: throw UserNotFoundException()
        return org.springframework.security.core.userdetails.User//
                .withUsername(username)
                .password(user.password)
                .authorities(user.roles)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build()
    }

}