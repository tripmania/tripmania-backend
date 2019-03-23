package com.chichkanov.backend.user.model

import org.springframework.security.core.GrantedAuthority

enum class Role: GrantedAuthority{

    ROLE_ADMIN, ROLE_CLIENT;

    override fun getAuthority(): String {
        return name
    }

}