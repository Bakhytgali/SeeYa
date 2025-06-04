package com.example.seeya.data.model

import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("_id") val eventId: String,
    val name: String,
    val description: String,
    val category: String,
    val eventPicture: String? = null,
    val isOpen: Boolean,
    val creator: Creator,
    val participants: List<String> = emptyList(),
    val location: String,
    val startDate: String,
    val createdAt: String,
    val attendance: List<String> = emptyList(),
    val eventTags: String,
)

