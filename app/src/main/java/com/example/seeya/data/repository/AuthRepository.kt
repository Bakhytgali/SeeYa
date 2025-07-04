package com.example.seeya.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.seeya.data.api.RetrofitClient
import com.example.seeya.data.model.LoginResponse
import com.example.seeya.data.model.LoginRequest
import com.example.seeya.data.model.SignInRequest
import com.example.seeya.data.model.UpdateProfileRequest
import com.example.seeya.data.model.User
import com.example.seeya.data.model.VerifyCodeRequest
import com.example.seeya.data.model.VerifyEmailRequest
import com.example.seeya.utils.TokenManager
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

class AuthRepository(private val context: Context) {
    private val api = RetrofitClient.createApiService(context)

    suspend fun loginUser(email: String, password: String): Response<LoginResponse> {
        // Получаем FCM токен
        val fcmToken = FirebaseMessaging.getInstance().token.await()
        TokenManager.saveFcmToken(context = context, token = fcmToken)

        // Отправляем его сразу при логине
        val response = api.loginUser(LoginRequest(email, password, fcmToken = fcmToken))

        if (response.isSuccessful) {
            response.body()?.let { authResponse ->
                TokenManager.saveToken(context, authResponse.token)
                TokenManager.saveUser(context, authResponse.user)
            }
        }

        return response
    }

    suspend fun registerUser(signInRequest: SignInRequest): String? {
        return try {
            Log.d("AuthRepository", "Making API call with request: ${signInRequest.copy(password = "***")}")

            val response = api.registerUser(signInRequest)
            Log.d("AuthRepository", "Received response. Code: ${response.code()}, isSuccessful: ${response.isSuccessful}")

            if (response.isSuccessful && response.code() == 200) {
                val responseBody = response.body()
                Log.d("AuthRepository", "Successful registration. Response body: $responseBody")
                responseBody
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("AuthRepository", "Registration failed. Error code: ${response.code()}, Error body: $errorBody")
                null
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "API call failed: ${e.message}", e)
            throw e
        }
    }

    suspend fun verifyEmail(email: String): Boolean {
        val request = VerifyEmailRequest(email)
        Log.d("MY log", "$request")
        val response = api.authVerifyEmail(request)
        return response.isSuccessful && response.code() == 200
    }

    suspend fun verifyCode(code: String, email: String): Boolean {
        val request = VerifyCodeRequest(code = code, email = email)
        val response = api.authVerifyCode(request)
        return response.isSuccessful && response.code() == 200
    }

    suspend fun restorePassword(email: String): Response<Unit> {
        return api.restorePassword(email)
    }

    suspend fun updateAccountInfo(token: String, request: UpdateProfileRequest): Result<User> {
        return try {
            val response = api.updateAccountInfo("Bearer $token", request)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadImageToCloudinary(imageUri: Uri, context: Context): String {
        return withContext(Dispatchers.IO) {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(imageUri)
                ?: throw IOException("Не удалось открыть изображение")

            val fileBytes = inputStream.readBytes()
            inputStream.close()

            if (fileBytes.isEmpty()) throw IOException("Изображение пустое")

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    "file", "image.jpg",
                    fileBytes.toRequestBody("image/jpeg".toMediaType())
                )
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
}
