package com.example.seeya.data.api

import com.example.seeya.data.model.Club
import com.example.seeya.data.model.CreateClubRequest
import com.example.seeya.data.model.CreateClubResponse
import com.example.seeya.data.model.CreateEventRequest
import com.example.seeya.data.model.CreateEventResponse
import com.example.seeya.data.model.Event
import com.example.seeya.data.model.EventApplication
import com.example.seeya.data.model.LoginResponse
import com.example.seeya.data.model.LoginRequest
import com.example.seeya.data.model.Participant
import com.example.seeya.data.model.QrDataModel
import com.example.seeya.data.model.SearchResponse
import com.example.seeya.data.model.SearchUser
import com.example.seeya.data.model.SignInRequest
import com.example.seeya.data.model.SignInResponse
import com.example.seeya.data.model.UpdateProfileRequest
import com.example.seeya.data.model.User
import com.example.seeya.data.model.VerifyCodeRequest
import com.example.seeya.data.model.VerifyEmailRequest
import com.example.seeya.data.repository.SearchRepository
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
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

        @GET("events/{eventId}/attendances")
        suspend fun getAttendanceList(@Path("eventId") eventId: String): Response<List<Participant>>

        @POST("users/check-attendance")
        suspend fun checkAttendance(@Body request: QrDataModel): Response<List<Participant>>

        // Join events
        @POST("users/join/{eventId}")
        suspend fun joinEvent(@Header("Authorization") token: String, @Path("eventId") eventId: String): Response<Unit>

        // Search User
        @GET("/search/users")
        suspend fun searchUser(@Query("query") value: String): Response<SearchResponse<List<SearchUser>>>

        @GET("/users/{userId}")
        suspend fun getUserById(@Path("userId") userId: String): Response<User>


        // CLUBS
        @GET("/clubs")
        suspend fun getMyClubs(@Header("Authorization") token: String): Response<List<Club>>

        @POST("/clubs/create")
        suspend fun createClub(
            @Header("Authorization") token: String,
            @Body club: CreateClubRequest
        ): String

        @GET("/clubs/{clubId}")
        suspend fun getClubById(@Path("clubId") clubId: String): Response<Club>

        @GET("/applications/event/{eventId}/applications")
        suspend fun getEventApplications(@Path("eventId") eventId: String): Response<List<EventApplication>>

        @POST("applications/accept/{applicationId}")
        suspend fun acceptApplication(@Path("applicationId") applicationId: String): Response<List<EventApplication>>

        @POST("applications/reject/{applicationId}")
        suspend fun rejectApplication(@Path("applicationId") applicationId: String): Response<List<EventApplication>>

        @POST("/profile/forgot-password/{email}")
        suspend fun restorePassword(@Path("email") email: String): Response<Unit>

        @PUT("/profile/update")
        suspend fun updateAccountInfo(
            @Header("Authorization") token: String,
            @Body request: UpdateProfileRequest): Response<User>
    }
}