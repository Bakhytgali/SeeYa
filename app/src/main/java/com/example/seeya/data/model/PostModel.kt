package com.example.seeya.data.model

import com.google.gson.annotations.SerializedName

data class PostModel(
    @SerializedName("_id") val id: String,
    val header: String,
    val text: String,
    val media: List<String> = emptyList(),
    val likes: List<String> = emptyList(),
    var likeCount: Int = 0,
    val authorId: Creator,
    val eventId: String,
)
