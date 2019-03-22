package com.chichkanov.backend.user

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class User(
        val email: String,
        val login: String,
        val password: String,
        val name: String? = null,
        val photoUrl: String? = null,
        val status: String? = null
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

}