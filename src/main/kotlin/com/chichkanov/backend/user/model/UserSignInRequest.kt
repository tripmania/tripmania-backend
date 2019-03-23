package com.chichkanov.backend.user.model

data class UserSignInRequest(
        val login: String,
        val password: String
)