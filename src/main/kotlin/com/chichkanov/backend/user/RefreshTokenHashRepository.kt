package com.chichkanov.backend.user

import com.chichkanov.backend.user.model.RefreshTokenHash
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenHashRepository : JpaRepository<RefreshTokenHash, Long> {

    fun findByUserId(userId: Long): List<RefreshTokenHash>

}