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
import retrofit2.http.Path

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

        // GET EVENTS
        @GET("events/")
        suspend fun getAllEvents(): Response<List<Event>>

        @GET("events/{eventId}")
        suspend fun getEvent(@Path("eventId") eventId: String): Response<Event>

        @GET("users/eventsCreated")
        suspend fun getMyEvents(@Header("Authorization") token: String): Response<List<Event>>

        // Join events
        @POST("users/join/{eventId}")
        suspend fun joinEvent(@Header("Authorization") token: String, @Path("eventId") eventId: String): Response<Unit>
    }
}