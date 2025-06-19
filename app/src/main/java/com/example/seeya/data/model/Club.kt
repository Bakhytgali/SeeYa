package com.example.seeya.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class Club(
    @SerializedName("_id") val id: String,
    val name: String,
    val description: String,
    val clubPicture: String? = "",
    val isOpen: Boolean,
    val creator: Creator,
    val participants: List<String> = emptyList(),
    val createdAt: LocalDate,
    val clubTags: String,
    val category: String,
)