package com.chichkanov.backend.user.model

data class TokenResponse(
        val accessToken: String,
        val refreshToken: String
)