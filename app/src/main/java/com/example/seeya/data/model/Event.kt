package com.example.seeya.data.model

import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("_id") val eventId: String,
    val name: String,
    val description: String,
    val category: String? = null,
    val eventPicture: String? = null,
    val isOpen: Boolean,
    val creator: Creator,
    val participants: List<String> = emptyList(),
    val location: String,
    val locationCoordinates: String,
    val startDate: String,
    val attendance: List<String> = emptyList(),
    val eventTags: String? = null,
    val eventRating: Double = 5.0,
    val ratingCount: Int = 0,
    val posts: List<String> = emptyList()
)
