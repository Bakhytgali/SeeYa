package com.example.seeya.data.api

import com.example.seeya.data.model.Club
import com.example.seeya.data.model.CreateEventRequest
import com.example.seeya.data.model.CreateEventResponse
import com.example.seeya.data.model.Event
import com.example.seeya.data.model.LoginResponse
import com.example.seeya.data.model.LoginRequest
import com.example.seeya.data.model.SearchResponse
import com.example.seeya.data.model.SearchUser
import com.example.seeya.data.model.SignInRequest
import com.example.seeya.data.model.SignInResponse
import com.example.seeya.data.model.User
import com.example.seeya.data.model.VerifyCodeRequest
import com.example.seeya.data.model.VerifyEmailRequest
import com.example.seeya.data.repository.SearchRepository
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    interface ApiService {

        // AUTHORIZATION
        @POST("auth/login")
        suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

        @POST("auth/register")
        suspend fun registerUser(@Body request: SignInRequest): Response<String>

        @POST("auth/verify/email")
        suspend fun authVerifyEmail(@Body request: VerifyEmailRequest): Response<Boolean>

        @POST("auth/verify/code")
        suspend fun authVerifyCode(@Body request: VerifyCodeRequest): Response<Boolean>

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

        // Search User
        @GET("/search/users")
        suspend fun searchUser(@Query("query") value: String): Response<SearchResponse<List<SearchUser>>>

        @GET("/users/{userId}")
        suspend fun getUserById(@Path("userId") userId: String): Response<User>

        @GET("/clubs")
        suspend fun getMyClubs(@Header("Authorization") token: String): Response<List<Club>>
    }
}