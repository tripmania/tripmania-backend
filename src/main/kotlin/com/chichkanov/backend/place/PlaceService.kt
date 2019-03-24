package com.chichkanov.backend.place

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service

@Service
class PlaceService {

    private companion object {
        private val placesData by lazy {
            ObjectMapper().registerModule(KotlinModule()).readValue<List<Place>>(ClassPathResource("places.json").file)
        }
        private const val MAX_RESPONSE_SIZE = 20
    }

    fun query(input: String): List<Place> {
        val inputLower = input.toLowerCase().replace("\\s+", " ")
        return placesData.filter {
            it.country.toLowerCase().contains(inputLower) ||
                    it.city.toLowerCase().contains(inputLower)
        }.take(MAX_RESPONSE_SIZE)
    }

}