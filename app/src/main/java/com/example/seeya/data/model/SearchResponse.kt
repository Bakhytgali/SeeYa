package com.example.seeya.data.model

import com.google.gson.annotations.SerializedName

data class SearchResponse<T>(
    @SerializedName("data") val items: T
)
