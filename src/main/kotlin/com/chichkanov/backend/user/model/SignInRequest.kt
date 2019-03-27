package com.chichkanov.backend.user.model

data class SignInRequest(
        val login: String,
        val password: String
)