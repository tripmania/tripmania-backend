package com.chichkanov.backend.user

import com.chichkanov.backend.user.error.UserNotFoundException
import com.chichkanov.backend.user.model.TokenResponse
import com.chichkanov.backend.user.model.User
import com.chichkanov.backend.user.model.UserSignInRequest
import com.chichkanov.backend.user.model.UserSignUpRequest
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