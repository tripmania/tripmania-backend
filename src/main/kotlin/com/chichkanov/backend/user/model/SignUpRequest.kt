package com.chichkanov.backend.user.model

data class SignUpRequest(
        val login: String,
        val email: String,
        val password: String,
        val name: String? = null
)