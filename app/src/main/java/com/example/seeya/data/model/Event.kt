package com.example.seeya.data.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Event(
    @SerializedName("_id") val eventId: String,
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

