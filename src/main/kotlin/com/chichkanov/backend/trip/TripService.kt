package com.chichkanov.backend.trip

import com.chichkanov.backend.error.InsufficientPrivilegesException
import com.chichkanov.backend.trip.error.TripNotFoundException
import com.chichkanov.backend.trip.model.Trip
import com.chichkanov.backend.trip.model.TripRequest
import com.chichkanov.backend.user.UserRepository
import com.chichkanov.backend.user.error.UserNotFoundException
import org.springframework.stereotype.Service

@Service
class TripService constructor(
        private val tripRepository: TripRepository,
        private val userRepository: UserRepository
) {

    fun getTripsByUser(userId: Long): List<Trip> {
        val user = userRepository.findById(userId).orElseThrow { UserNotFoundException() }
        return tripRepository.findByUserId(user.id)
    }

    fun getTripById(tripId: Long, userLogin: String): Trip {
        return tripRepository.findById(tripId).orElseThrow { TripNotFoundException() }
    }

    fun addTrip(tripRequest: TripRequest, userLogin: String): Trip {
        val user = userRepository.findByLogin(userLogin) ?: throw UserNotFoundException()
        val trip = Trip(
                user.id,
                tripRequest.title,
                tripRequest.startDate,
                tripRequest.endDate,
                tripRequest.photoUrl,
                tripRequest.path
        )
        return tripRepository.save(trip)
    }

    fun updateTrip(tripRequest: TripRequest, userLogin: String): Trip {
        if (tripRequest.id == null) {
            throw TripNotFoundException()
        }
        val trip = tripRepository.findById(tripRequest.id).orElseThrow { TripNotFoundException() }
        tripRepository.findById(trip.id).orElseThrow { TripNotFoundException() }
        checkTripEditUserPrivileges(trip, userLogin)

        val updatedTrip = Trip(
                trip.userId,
                tripRequest.title,
                tripRequest.startDate,
                tripRequest.endDate,
                tripRequest.photoUrl,
                tripRequest.path
        ).apply { id = trip.id }

        return tripRepository.save(updatedTrip)
    }

    fun deleteTrip(tripId: Long, userLogin: String) {
        tripRepository.findById(tripId).ifPresent { checkTripEditUserPrivileges(it, userLogin) }
        tripRepository
                .findById(tripId)
                .ifPresent { tripRepository.delete(it) }
    }

    private fun checkTripEditUserPrivileges(trip: Trip, userLogin: String) {
        val user = userRepository.findByLogin(userLogin) ?: throw UserNotFoundException()
        if (trip.userId != user.id) {
            throw InsufficientPrivilegesException()
        }
    }

}