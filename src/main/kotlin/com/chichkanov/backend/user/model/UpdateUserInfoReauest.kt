package com.chichkanov.backend.user.model

data class UpdateUserInfoReauest(
        val status: String?,
        val photoUrl: String?,
        val name: String?
)