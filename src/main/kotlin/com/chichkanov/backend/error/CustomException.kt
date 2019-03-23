package com.chichkanov.backend.error

import org.springframework.http.HttpStatus

class CustomException(
        val httpStatus: HttpStatus,
        message: String
) : RuntimeException(message)