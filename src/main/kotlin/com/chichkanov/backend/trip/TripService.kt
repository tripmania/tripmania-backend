package com.chichkanov.backend.trip

import com.chichkanov.backend.user.UserNotFoundException
import com.chichkanov.backend.user.UserRepository
import org.springframework.stereotype.Service

@Service
class TripService constructor(
        private val tripRepository: TripRepository,
        private val userRepository: UserRepository
) {

    fun getTripsByUser(userId: Long): List<Trip> {
        if (userRepository.findById(userId).isEmpty) {
            throw UserNotFoundException()
        }
        return tripRepository.findByUserId(userId)
    }

    fun addTrip(trip: Trip): Trip {
        if (userRepository.findById(trip.userId).isEmpty) {
            throw UserNotFoundException()
        }
        return tripRepository.save(trip)
    }

}