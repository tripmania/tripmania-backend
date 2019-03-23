package com.chichkanov.backend.user.error

import com.chichkanov.backend.error.CustomException
import org.springframework.http.HttpStatus

class UserNotFoundException : CustomException(HttpStatus.NOT_FOUND, "User not found")