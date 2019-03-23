package com.chichkanov.backend.trip

import com.chichkanov.backend.security.JwtTokenProvider
import com.chichkanov.backend.trip.model.TripRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/trips")
class TripController constructor(
        private val tripService: TripService,
        private val jwtTokenProvider: JwtTokenProvider
) {

    @GetMapping
    fun getTripsByUser(request: HttpServletRequest): ResponseEntity<List<Trip>> {
        val login = jwtTokenProvider.getLogin(request)!!
        return ResponseEntity.ok(tripService.getTripsByUser(login))
    }

    @GetMapping("/{tripId}")
    fun getTripById(@PathVariable("tripId") tripId: Long, request: HttpServletRequest): ResponseEntity<Trip> {
        val userLogin = jwtTokenProvider.getLogin(request)!!
        return ResponseEntity.ok(tripService.getTripById(tripId, userLogin))
    }

    @PostMapping
    fun addTrip(@RequestBody tripRequest: TripRequest, request: HttpServletRequest): ResponseEntity<Trip> {
        val userLogin = jwtTokenProvider.getLogin(request)!!
        return ResponseEntity.ok(tripService.addTrip(tripRequest, userLogin))
    }

    @PutMapping
    fun updateTrip(@RequestBody tripRequest: TripRequest, request: HttpServletRequest): ResponseEntity<Trip> {
        val userLogin = jwtTokenProvider.getLogin(request)!!
        return ResponseEntity.ok(tripService.updateTrip(tripRequest, userLogin))
    }

    @DeleteMapping("/{tripId}")
    fun deleteTrip(@PathVariable("tripId") tripId: Long, request: HttpServletRequest) {
        val userLogin = jwtTokenProvider.getLogin(request)!!
        tripService.deleteTrip(tripId, userLogin)
    }

}