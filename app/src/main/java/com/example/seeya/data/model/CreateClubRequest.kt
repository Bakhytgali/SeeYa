package com.example.seeya.data.model

import java.time.LocalDate

data class CreateClubRequest(
    val name: String,
    val isOpen: Boolean,
    val clubTags: String,
    val clubPicture: String,
    val createdAt: LocalDate = LocalDate.now(),
    val description: String,
    val category: String,
    val participants: List<String> = emptyList(),
    val creator: Creator
)
