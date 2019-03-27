package com.chichkanov.backend.error

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletResponse

@ControllerAdvice
class CommonExceptionHandler {

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(res: HttpServletResponse, e: CustomException) {
        res.sendError(e.httpStatus.value(), e.message)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(res: HttpServletResponse, e: Exception) {
        res.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.message ?: "Something went wrong")
    }

}