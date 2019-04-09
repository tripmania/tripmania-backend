package com.chichkanov.backend.user.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
data class User constructor(
        @JsonIgnore
        @Column(unique = true)
        val email: String,
        val login: String,
        @JsonIgnore
        val password: String,
        val name: String? = null,
        val photoUrl: String? = null,
        val status: String? = null,
        @JsonIgnore
        @ElementCollection(fetch = FetchType.EAGER)
        val roles: Set<Role> = setOf(Role.ROLE_CLIENT)
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

}