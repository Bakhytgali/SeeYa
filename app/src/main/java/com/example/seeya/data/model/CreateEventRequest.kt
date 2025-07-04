package com.example.seeya.data.model

import java.time.LocalDateTime

data class CreateEventRequest(
    val creator: Creator,
    val name: String,
    val category: String,
    val description: String,
    val location: String,
    val locationCoordinates: String,
    val eventPicture: String?,
    val isOpen: Boolean,
    val startDate: String,
    val eventTags: String,
)

