package com.chichkanov.backend.trip

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/trips")
class TripController constructor(
        private val tripService: TripService
) {

    @GetMapping("/user/{userId}")
    fun getTripsByUser(@PathVariable("userId") userId: Long): ResponseEntity<List<Trip>> {
        return ResponseEntity.ok(tripService.getTripsByUser(userId))
    }

    @GetMapping("/{tripId}")
    fun getTripById(@PathVariable("tripId") tripId: Long): ResponseEntity<Trip> {
        return ResponseEntity.ok(tripService.getTripById(tripId))
    }

    @PostMapping
    fun addTrip(@RequestBody trip: Trip): ResponseEntity<Trip> {
        return ResponseEntity.ok(tripService.addTrip(trip))
    }

    @PutMapping
    fun updateTrip(@RequestBody trip: Trip): ResponseEntity<Trip> {
        return ResponseEntity.ok(tripService.updateTrip(trip))
    }

    @DeleteMapping("/{tripId}")
    fun deleteTrip(@PathVariable("tripId") tripId: Long) {
        tripService.deleteTrip(tripId)
    }

}