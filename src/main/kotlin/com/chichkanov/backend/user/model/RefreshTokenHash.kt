package com.chichkanov.backend.user.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import javax.persistence.*

@Entity
data class RefreshTokenHash(
        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "post_id", nullable = false)
        @OnDelete(action = OnDeleteAction.CASCADE)
        @JsonIgnore
        val user: User,
        val tokenHash: Int
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

}