package com.chichkanov.backend.user

import com.chichkanov.backend.security.JwtTokenProvider
import com.chichkanov.backend.user.model.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/users")
class UserController constructor(
        private val userService: UserService,
        private val jwtTokenProvider: JwtTokenProvider
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

    @RequestMapping("/{id}", "")
    fun getUserById(@PathVariable("id", required = false) id: Long?, request: HttpServletRequest): ResponseEntity<User> {
        return ResponseEntity.ok(
                if (id != null) {
                    userService.getUserById(id)
                } else {
                    val userLogin = jwtTokenProvider.getLogin(request)!!
                    userService.gerUserByLogin(userLogin)
                }
        )
    }

    @GetMapping("/all")
    fun getUserById(): ResponseEntity<List<User>> {
        return ResponseEntity.ok(userService.getAllUsers())
    }

}