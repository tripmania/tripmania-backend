package com.chichkanov.backend.trip

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Trip(
        @JsonIgnore
        val userId: Long,
        val title: String,
        val startDate: Long,
        val endDate: Long,
        val photoUrl: String? = null
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

}