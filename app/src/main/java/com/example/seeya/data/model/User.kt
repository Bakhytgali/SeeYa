package com.example.seeya.data.model

import java.util.Date

data class User(
    val id: String,
    var name: String,
    var surname: String,
    val email: String,
    val password: String,
    val isAdmin: Boolean,
    var profilePicture: String,
    var visitedEvents: List<Event>,
    var joinedClubs: List<Club>,
    val createdAt: Date
)
