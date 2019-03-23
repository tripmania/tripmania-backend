package com.chichkanov.backend.user

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/users")
class UserController constructor(
        private val userService: UserService
) {

    @PostMapping("/sign-up")
    fun signUp(@RequestBody userSignUpRequest: UserSignUpRequest): TokenResponse {
        return userService.signUp(userSignUpRequest)
    }

    @PostMapping("/sign-in")
    fun signIn(@RequestBody userSignInRequest: UserSignInRequest): TokenResponse {
        return userService.signIn(userSignInRequest)
    }

    @GetMapping("/refresh")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    fun refreshToken(req: HttpServletRequest): TokenResponse {
        return userService.refreshToken(req.remoteUser)
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

data class UserSignUpRequest(
        val login: String,
        val email: String,
        val password: String,
        val name: String? = null
)

data class UserSignInRequest(
        val login: String,
        val password: String
)

data class TokenResponse(
        val token: String
)