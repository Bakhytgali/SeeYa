package com.example.seeya.data.model

data class LoginRequest(
    val email: String,
    val password: String,
    val fcmToken: String,
)
