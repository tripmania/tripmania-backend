package com.chichkanov.backend.error

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import java.util.*

@ControllerAdvice
class CommonExceptionHandler {

    @ExceptionHandler(RuntimeException::class)
    fun handleException(exception: Exception, webRequest: WebRequest): ResponseEntity<Any> {
        val errorMessage = ErrorMessage(
                Date(),
                exception.message ?: "Unknown error"
        )
        return ResponseEntity(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR)
    }

}