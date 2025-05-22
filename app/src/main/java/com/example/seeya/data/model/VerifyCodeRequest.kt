package com.example.seeya.data.model

data class VerifyCodeRequest(
    val email: String,
    val code: String
)
