package com.example.seeya.data.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class User(
    @SerializedName("_id") val id: String,
    var name: String,
    var surname: String,
    val email: String,
    val username: String,
    val password: String,
    val isAdmin: Boolean,
    var profilePicture: String,
    var visitedEvents: List<Event>,
    var joinedClubs: List<Club>,
    val createdAt: Date
)
