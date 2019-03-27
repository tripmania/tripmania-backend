package com.chichkanov.backend.trip

import com.chichkanov.backend.trip.model.Trip
import org.springframework.data.jpa.repository.JpaRepository

interface TripRepository : JpaRepository<Trip, Long> {

    fun findByUserId(userId: Long): List<Trip>

}