package com.example.seeya.data.repository

import android.content.Context
import com.example.seeya.data.api.APIService
import com.example.seeya.data.api.RetrofitClient
import com.example.seeya.data.model.CreateEventRequest
import com.example.seeya.data.model.CreateEventResponse
import com.example.seeya.data.model.Event
import com.example.seeya.data.model.Participant
import com.example.seeya.data.model.QrDataModel
import com.example.seeya.utils.TokenManager
import retrofit2.Response
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class EventRepository(private val context: Context) {

    private val api = RetrofitClient.createApiService(context)

    suspend fun createEvent(event: CreateEventRequest): Response<CreateEventResponse>? {
        val token = TokenManager.getToken(context)

        return try {
            if (token.isNullOrEmpty()) {
                throw Exception("No auth token found.")
            }
            val response: Response<CreateEventResponse> = api.createEvent("Bearer $token", event)

            if (response.isSuccessful) {
                response
            } else {
                throw Exception("Failed to create event: ${response.message()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getAllEvents(): Response<List<Event>>? {
        return try {
            val response: Response<List<Event>> = api.getAllEvents()

            if (response.isSuccessful) {
                response
            } else {
                throw Exception("Failed to fetch events: ${response.message()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getMyEvents(): Response<List<Event>>? {
        return try {
            val token = TokenManager.getToken(context)

            if (token.isNullOrEmpty()) {
                throw Exception("No auth token found.")
            }
            val response: Response<List<Event>> = api.getMyEvents("Bearer $token")

            if (response.isSuccessful) {
                response
            } else {
                throw Exception("Failed to fetch your events: ${response.message()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getEvent(eventId: String): Response<Event>? {
        return try {
            val response: Response<Event> = api.getEvent(eventId)

            if (response.isSuccessful) {
                response
            } else {
                throw Exception("Failed to fetch an event!: ${response.message()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun joinEvent(eventId: String): Response<Unit>? {
        return try {
            val token = TokenManager.getToken(context)

            if (token.isNullOrEmpty()) {
                throw Exception("No auth token found.")
            }

            val response: Response<Unit> = api.joinEvent("Bearer $token", eventId = eventId)

            if (response.isSuccessful) {
                response
            } else {
                throw Exception("Failed to join the event!: ${response.message()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getAttendance(eventId: String): List<Participant> {
        return try {
            val response = api.getAttendanceList(eventId)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun checkAttendance(qrDataModel: QrDataModel): Response<List<Event>> {
        return api.checkAttendance(qrDataModel)
    }
}
