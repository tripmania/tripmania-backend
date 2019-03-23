package com.chichkanov.backend.error

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.io.IOException
import javax.servlet.http.HttpServletResponse

@ControllerAdvice
class CommonExceptionHandler {

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(res: HttpServletResponse, ex: CustomException) {
        res.sendError(ex.httpStatus.value(), ex.message)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(res: HttpServletResponse) {
        res.sendError(HttpStatus.BAD_REQUEST.value(), "Something went wrong")
    }

}