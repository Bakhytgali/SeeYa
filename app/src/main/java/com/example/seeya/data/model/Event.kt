package com.example.seeya.data.model

import java.util.Date

data class Event(
    val name: String,
    val description: String,
    val category: String,
    val eventPicture: String,
    val isClosed: Boolean,
    val creator: Creator,
    val participants: List<Participant>,
    val location: String,
    val startDate: Date,
    val endDate: Date,
    val createdAt: Date
)

