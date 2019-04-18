package com.chichkanov.backend.publication

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Publication(
        val tripId: Long,
        val tripName: String,
        val publisherId: Long,
        val publisherLogin: String,
        val publisherPhotoUrl: String?,
        val publishDate: Long,
        val description: String?,
        val photoUrl: String?,
        val placeName: String?,
        val likesCount: Long,
        val commentsCount: Long
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

}