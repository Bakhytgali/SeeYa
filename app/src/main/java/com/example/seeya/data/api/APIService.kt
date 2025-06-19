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
import com.example.seeya.data.model.PostModel
import com.example.seeya.data.model.PostRequestModel
import com.example.seeya.data.model.QrDataModel
import com.example.seeya.data.model.RatingRequest
import com.example.seeya.data.model.SearchResponse
import com.example.seeya.data.model.SearchUser
import com.example.seeya.data.model.SignInRequest
import com.example.seeya.data.model.SignInResponse
import com.example.seeya.data.model.UpdateEventRequest
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

        @GET("users/joinedEvents")
        suspend fun getVisitedEvents(@Header("Authorization") token: String): Response<List<Event>>

        @GET("events/{eventId}")
        suspend fun getEvent(@Path("eventId") eventId: String): Response<Event>

        @POST("events/rate/{eventId}")
        suspend fun rateEvent(
            @Path("eventId") eventId: String,
            @Body ratingRequest: RatingRequest,
            @Header("Authorization") token: String
        ): Response<String>


        @POST("posts/create/{eventId}")
        suspend fun addPost(
            @Header("Authorization") token: String,
            @Path("eventId") eventId: String,
            @Body post: PostRequestModel
        ): Response<Unit>

        @GET("posts/{postId}")
        suspend fun getPost(@Path("postId") postId: String): Response<PostModel>

        @GET("posts/event/{eventId}")
        suspend fun fetchPosts(@Path("eventId") eventId: String): Response<List<PostModel>>

        @POST("posts/like/{eventId}")
        suspend fun likePost(@Path("eventId") eventId: String, @Header("Authorization") token: String): Response<List<String>>

        @POST("posts/unlike/{eventId}")
        suspend fun unlikePost(@Path("eventId") eventId: String, @Header("Authorization") token: String): Response<List<String>>

        @GET("users/eventsCreated")
        suspend fun getMyEvents(@Header("Authorization") token: String): Response<List<Event>>

        @GET("events/{eventId}/attendances")
        suspend fun getAttendanceList(@Path("eventId") eventId: String): Response<List<Participant>>

        @POST("users/check-attendance")
        suspend fun checkAttendance(@Body request: QrDataModel): Response<List<String>>

        @GET("posts/media/{eventId}")
        suspend fun getEventMedia(@Path("eventId") eventId: String): Response<List<String>>

        // Join events
        @POST("users/join/{eventId}")
        suspend fun joinEvent(@Header("Authorization") token: String, @Path("eventId") eventId: String): Response<Unit>

        // SEARCHING
        // Search User
        @GET("/search/users")
        suspend fun searchUser(@Query("query") value: String): Response<SearchResponse<List<SearchUser>>>

        // Search Events
        @GET("/search/events")
        suspend fun searchEvents(@Query("query") value: String): Response<SearchResponse<List<Event>>>

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

        @PUT("/events/update/{eventId}")
        suspend fun updateEventInfo(
            @Header("Authorization") token: String,
            @Body request: UpdateEventRequest,
            @Path("eventId") eventId: String
        ): Response<Unit>

    }
}