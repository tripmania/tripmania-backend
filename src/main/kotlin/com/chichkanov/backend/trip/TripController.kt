package com.chichkanov.backend.trip

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/trips")
class TripController constructor(
        private val tripService: TripService
) {

    @GetMapping("/{userId}")
    fun getTripsByUser(@PathVariable("userId") userId: Long): ResponseEntity<List<Trip>> {
        return ResponseEntity.ok(tripService.getTripsByUser(userId))
    }

    @PostMapping
    fun addTrip(@RequestBody trip: Trip): ResponseEntity<Trip> {
        return ResponseEntity.ok(tripService.addTrip(trip))
    }

    fun deleteTrip() {

    }

}