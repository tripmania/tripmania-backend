package com.chichkanov.backend.trip.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.CollectionType
import javax.persistence.*

@Entity
data class Trip(
        @JsonIgnore
        val userId: Long,
        val title: String,
        val startDate: Long,
        val endDate: Long,
        val photoUrl: String? = null,
        @ElementCollection
        val path: List<String> = emptyList()
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

}