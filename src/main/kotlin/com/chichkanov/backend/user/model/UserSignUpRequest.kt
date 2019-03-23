package com.chichkanov.backend.user.model

data class UserSignUpRequest(
        val login: String,
        val email: String,
        val password: String,
        val name: String? = null
)