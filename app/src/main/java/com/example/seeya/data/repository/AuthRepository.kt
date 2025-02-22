package com.example.seeya.data.repository

import android.content.Context
import com.example.seeya.data.api.RetrofitClient
import com.example.seeya.data.model.LoginResponse
import com.example.seeya.data.model.LoginRequest
import com.example.seeya.data.model.SignInRequest
import com.example.seeya.utils.TokenManager
import retrofit2.Response

class AuthRepository(private val context: Context) {
    private val api = RetrofitClient.createApiService(context)

    suspend fun loginUser(email: String, password: String): Response<LoginResponse> {
        val response = api.loginUser(LoginRequest(email, password))

        if (response.isSuccessful) {
            response.body()?.let { authResponse ->
                TokenManager.saveToken(context, authResponse.token) // Сохраняем токен
                TokenManager.saveUser(context, authResponse.user)   // Сохраняем юзера
            }
        }

        return response
    }

    suspend fun registerUser(signInRequest: SignInRequest): String? {
        val response = api.registerUser(signInRequest)

        return if(response.isSuccessful && response.code() == 201) {
            response.body()?.message
        } else {
            null
        }
    }
}
