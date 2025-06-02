package com.example.seeya.data.model

data class UpdateProfileRequest(
    val username: String?,
    val profilePicture: String?,
    val name: String?,
    val surname: String?,
    val password: String?
)
