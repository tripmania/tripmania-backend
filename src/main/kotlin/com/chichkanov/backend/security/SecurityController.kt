package com.chichkanov.backend.security

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/security")
class SecurityController constructor(
        private val securityService: SecurityService
) {

    @PostMapping("/sign-up")
    fun signUp(@RequestBody securityUser: SecurityUser) = securityService.signUp(securityUser)

    @PostMapping("/sign-in")
    fun signIn(@RequestBody securityUser: SecurityUser) = securityService.signIn(securityUser)

}