package com.example.seeya.data.model

import java.util.Date

data class Club(
    val name: String,
    val description: String,
    val clubPicture: String,
    val isClosed: Boolean,
    val creatorId: String,
    val participants: List<Participant>,
    val createdAt: Date,
)