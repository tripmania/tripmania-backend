package com.chichkanov.backend.user

import org.springframework.stereotype.Service

@Service
class UserService constructor(
        private val userRepository: UserRepository
) {

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun getUserById(id: Long): User? {
        return userRepository.findById(id).orElse(null)
    }

}