package com.chichkanov.backend.user

import com.chichkanov.backend.user.error.UserNotFoundException
import com.chichkanov.backend.user.model.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController constructor(
        private val userService: UserService
) {

    @PostMapping("/sign-up")
    fun signUp(@RequestBody signUpRequest: SignUpRequest): TokenResponse {
        return userService.signUp(signUpRequest)
    }

    @PostMapping("/sign-in")
    fun signIn(@RequestBody signInRequest: SignInRequest): TokenResponse {
        return userService.signIn(signInRequest)
    }

    @PostMapping("/refresh")
    fun refreshToken(@RequestBody refreshTokenRequest: RefreshTokenRequest): TokenResponse {
        return userService.refreshToken(refreshTokenRequest)
    }

    @RequestMapping("/{id}")
    fun getUserById(@PathVariable("id") id: Long): ResponseEntity<User> {
        val user = userService.getUserById(id) ?: throw UserNotFoundException()
        return ResponseEntity.ok(user)
    }

    @GetMapping("/all")
    fun getUserById(): ResponseEntity<List<User>> {
        return ResponseEntity.ok(userService.getAllUsers())
    }

}