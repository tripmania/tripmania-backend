package com.chichkanov.backend.trip

import org.springframework.data.jpa.repository.JpaRepository

interface TripRepository : JpaRepository<Trip, Long> {

    fun findByUserId(userId: Long): List<Trip>

}