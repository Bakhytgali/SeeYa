package com.example.seeya.data.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class User(
    @SerializedName("id") val id: String? = null,
    var name: String,
    var surname: String,
    val email: String,
    val username: String,
    val isAdmin: Boolean,
    val interestedTags: List<String>,
    var profilePicture: String?,
    var visitedEvents: List<String>,
    var joinedClubs: List<String> = emptyList(),
    val createdAt: Date,
    val rating: Double,
)
