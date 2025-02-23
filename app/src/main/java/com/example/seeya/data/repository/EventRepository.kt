package com.example.seeya.data.repository

import android.content.Context
import com.example.seeya.data.api.APIService
import com.example.seeya.data.api.RetrofitClient
import com.example.seeya.data.model.CreateEventRequest
import com.example.seeya.data.model.CreateEventResponse
import com.example.seeya.data.model.Event
import com.example.seeya.utils.TokenManager
import retrofit2.Response

class EventRepository(private val context: Context) {

    private val api = RetrofitClient.createApiService(context)

    suspend fun createEvent(event: CreateEventRequest): Response<CreateEventResponse>? {
        return try {
            val token = TokenManager.getToken(context)
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

            if(response.isSuccessful) {
                response
            } else {
                throw Exception("Failed to fetch events: ${response.message()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
