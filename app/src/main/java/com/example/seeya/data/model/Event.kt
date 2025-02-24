package com.example.seeya.data.model

import java.util.Date

data class Event(
    val eventId: String,
    val name: String,
    val description: String,
    val category: String,
    val eventPicture: String? = null,
    val isClosed: Boolean,
    val creator: Creator,
    val participants: List<Participant>?,
    val location: String,
    val startDate: Date,
    val createdAt: Date
)

