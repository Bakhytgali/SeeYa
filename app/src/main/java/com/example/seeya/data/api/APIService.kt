package com.example.seeya.data.api

import com.example.seeya.data.model.CreateEventRequest
import com.example.seeya.data.model.CreateEventResponse
import com.example.seeya.data.model.Event
import com.example.seeya.data.model.LoginResponse
import com.example.seeya.data.model.LoginRequest
import com.example.seeya.data.model.SignInRequest
import com.example.seeya.data.model.SignInResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface APIService {
    interface ApiService {

        // AUTHORIZATION
        @POST("auth/login")
        suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

        @POST("auth/register")
        suspend fun registerUser(@Body request: SignInRequest): Response<SignInResponse>



        // CREATE AN EVENT
        @POST("events/create")
        suspend fun createEvent(@Header("Authorization") token: String, @Body request: CreateEventRequest): Response<CreateEventResponse>



        // GET ALL EVENTS
        @GET("events/")
        suspend fun getAllEvents(): Response<List<Event>>
    }
}