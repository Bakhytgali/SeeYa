package com.example.seeya.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.seeya.data.api.APIService
import com.example.seeya.data.api.RetrofitClient
import com.example.seeya.data.model.CreateEventRequest
import com.example.seeya.data.model.CreateEventResponse
import com.example.seeya.data.model.Event
import com.example.seeya.data.model.EventApplication
import com.example.seeya.data.model.Participant
import com.example.seeya.data.model.QrDataModel
import com.example.seeya.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
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

    suspend fun uploadImageToCloudinary(imageUri: Uri): String {
        return withContext(Dispatchers.IO) {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(imageUri)
                ?: throw IOException("Failed to open image input stream")

            val fileBytes = inputStream.readBytes()
            inputStream.close()

            if (fileBytes.isEmpty()) throw IOException("Image is empty")

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "image.jpg", fileBytes.toRequestBody("image/jpeg".toMediaType()))
                .addFormDataPart("upload_preset", "seeya_application")
                .build()

            val request = Request.Builder()
                .url("https://api.cloudinary.com/v1_1/dghebgxse/image/upload")
                .post(requestBody)
                .build()

            val client = OkHttpClient()
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                val body = response.body?.string()
                throw IOException("Upload failed: ${response.code} - ${body ?: "No body"}")
            }

            val json = JSONObject(response.body?.string() ?: "")
            json.getString("secure_url")
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

    suspend fun checkAttendance(qrDataModel: QrDataModel): Response<List<Participant>> {
        return api.checkAttendance(qrDataModel)
    }

    suspend fun getEventApplications(eventId: String): Response<List<EventApplication>> {
        return try {
            api.getEventApplications(eventId)
        } catch (e: Exception) {
            Response.error(500, okhttp3.ResponseBody.create(null, "Exception: ${e.message}"))
        }
    }

    suspend fun acceptApplication(applicationId: String): List<EventApplication>? {
        return try {
            val response = api.acceptApplication(applicationId)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun rejectApplication(applicationId: String): List<EventApplication>? {
        return try {
            val response = api.rejectApplication(applicationId)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
