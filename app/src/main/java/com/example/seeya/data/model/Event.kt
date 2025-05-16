package com.example.seeya.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.util.Date

data class Event(
    @SerializedName("_id") val eventId: String,
    val name: String,
    val description: String,
    val category: String,
    val eventPicture: String? = null,
    val isOpen: Boolean,
    val creator: Creator,
    val participants: List<Participant>?,
    val location: String,
    val startDate: String,
    val endDate: String? = null,
    val createdAt: String
)

