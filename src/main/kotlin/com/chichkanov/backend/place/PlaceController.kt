package com.chichkanov.backend.place

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/places")
class PlaceController constructor(
        private val placeService: PlaceService
) {

    @GetMapping
    fun query(@RequestParam(value = "q", required = false) input: String?): ResponseEntity<List<Place>> {
        return ResponseEntity.ok(placeService.query(input ?: ""))
    }

}