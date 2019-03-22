package com.chichkanov.backend.trip

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Trip(
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