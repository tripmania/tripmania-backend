package com.chichkanov.backend.user

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController constructor(
        private val userService: UserService
) {

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