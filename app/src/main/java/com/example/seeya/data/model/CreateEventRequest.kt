package com.example.seeya.data.model

import java.util.Date

data class CreateEventRequest(
    val creator: Creator,
    val name: String,
    val category: String,
    val description: String,
    val location: String,
    val eventPicture: String?,
    val isOpen: Boolean,
    val startDate: Date
)

