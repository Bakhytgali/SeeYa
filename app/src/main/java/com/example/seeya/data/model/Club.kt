package com.example.seeya.data.model

import java.util.Date

data class Club(
    val name: String,
    val description: String,
    val clubPicture: String,
    val isClosed: Boolean,
    val creatorId: String,
    val participants: List<String>,
    val createdAt: Date,
)