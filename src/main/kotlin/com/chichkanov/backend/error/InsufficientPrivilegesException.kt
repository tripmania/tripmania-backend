package com.chichkanov.backend.error

import org.springframework.http.HttpStatus

class InsufficientPrivilegesException : CustomException(HttpStatus.FORBIDDEN, "Insufficient privileges")