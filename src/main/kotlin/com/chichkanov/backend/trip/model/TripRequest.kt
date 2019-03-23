package com.chichkanov.backend.trip.model

data class TripRequest(
        val title: String,
        val startDate: Long,
        val endDate: Long,
        val photoUrl: String? = null,
        val id: Long? = null
)