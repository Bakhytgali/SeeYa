package com.example.seeya.data.model

import kotlinx.serialization.Serializable

@Serializable
data class QrCodeData(
    val userId: String,
    val eventId: String,
    val timestamp: String,
)
