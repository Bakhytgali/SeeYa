package com.example.seeya.data.model

data class PostRequestModel(
    val header: String,
    val text: String,
    val media: List<String> = emptyList(),
    val date: String
)
