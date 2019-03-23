package com.chichkanov.backend.trip.error

import com.chichkanov.backend.error.CustomException
import org.springframework.http.HttpStatus

class TripNotFoundException : CustomException(HttpStatus.NOT_FOUND, "Trip not found")