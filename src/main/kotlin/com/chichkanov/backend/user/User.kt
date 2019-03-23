package com.chichkanov.backend.user

import javax.persistence.*

@Entity
data class User constructor(
        @Column(unique = true)
        val email: String,
        val login: String,
        val password: String,
        val name: String? = null,
        val photoUrl: String? = null,
        val status: String? = null,
        @ElementCollection(fetch = FetchType.EAGER)
        val roles: Set<Role> = setOf(Role.ROLE_CLIENT)
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

}