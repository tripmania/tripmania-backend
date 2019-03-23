package com.chichkanov.backend.user

import com.chichkanov.backend.user.model.User
import org.springframework.data.jpa.repository.JpaRepository


interface UserRepository : JpaRepository<User, Long> {

    fun existsByLogin(login: String): Boolean

    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String): User?

    fun findByLogin(login: String): User?

}