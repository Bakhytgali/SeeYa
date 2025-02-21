package com.example.seeya.data.api

import com.example.seeya.data.model.AuthResponse
import com.example.seeya.data.model.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIService {
    interface ApiService {
        @POST("auth/login")
        suspend fun loginUser(@Body request: LoginRequest): Response<AuthResponse>
    }
}