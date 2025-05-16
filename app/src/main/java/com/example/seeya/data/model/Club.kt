package com.example.seeya.data.model

import java.time.LocalDate

data class Club(
    val name: String,
    val description: String,
    val clubPicture: String,
    val isClosed: Boolean,
    val creatorId: String,
    val participants: List<Participant>?,
    val createdAt: LocalDate,
)