package com.example.seeya.data.model

data class SignInRequest(
    val name: String,
    val surname: String,
    val username: String,
    val email: String,
    val password: String
)
