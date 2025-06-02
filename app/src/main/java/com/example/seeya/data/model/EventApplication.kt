package com.example.seeya.data.model

import com.google.gson.annotations.SerializedName

data class EventApplication(
    @SerializedName("_id") val id: String?,
    val applicant: Applicant?,
    val targetType: String?,
    val targetId: String?
)

