package com.chichkanov.backend.trip

import com.chichkanov.backend.user.UserNotFoundException
import com.chichkanov.backend.user.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus

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

    fun getTripById(tripId: Long): Trip {
        val trip = tripRepository.findById(tripId)
        if (trip.isEmpty) {
            throw TripNotFoundException()
        }
        return trip.get()
    }

    fun addTrip(trip: Trip): Trip {
        if (userRepository.findById(trip.userId).isEmpty) {
            throw UserNotFoundException()
        }

        return tripRepository.save(trip)
    }

    fun updateTrip(trip: Trip): Trip {
        if (userRepository.findById(trip.userId).isEmpty) {
            throw UserNotFoundException()
        }

        val oldTrip = tripRepository.findById(trip.id)
        if (oldTrip.isEmpty) {
            throw TripNotFoundException()
        }

        return tripRepository.save(trip)
    }

    fun deleteTrip(tripId: Long) {
        tripRepository
                .findById(tripId)
                .ifPresent { tripRepository.delete(it) }
    }

}

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Trip not found")
class TripNotFoundException : RuntimeException()