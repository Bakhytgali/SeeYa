package com.example.seeya.data.model

import com.google.gson.annotations.SerializedName

data class Participant(
    @SerializedName("_id") val id: String,
    val name: String,
    val surname: String,
    val username: String
)
