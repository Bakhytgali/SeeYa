package com.example.seeya.data.model

data class UpdateEventRequest(
    val newTitle: String?,
    val newDescription: String?,
    val newCoordinates: String?,
    val newLocation: String?,
    val newPicture: String?
)
