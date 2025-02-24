package com.example.seeya.data.model

import com.google.gson.annotations.SerializedName

data class Creator(
    @SerializedName("_id")val id: String,
    val name: String,
    val surname: String,
    val rating: Double?,
    val username: String
)
