package com.example.seeya.data.repository

import android.content.Context
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
import retrofit2.Response

class AuthRepository(private val context: Context) {
    private val api = RetrofitClient.createApiService(context)

    suspend fun loginUser(email: String, password: String): Response<LoginResponse> {
        val response = api.loginUser(LoginRequest(email, password))

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


}
