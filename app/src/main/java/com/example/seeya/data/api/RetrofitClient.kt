package com.example.seeya.data.api

import android.content.Context
import com.example.seeya.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://172.20.10.11:8080/"

    // Функция для создания HTTP-клиента с токеном
    fun createClient(context: Context): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val authInterceptor = Interceptor { chain ->
            val token = TokenManager.getToken(context)
            val request = chain.request().newBuilder()

            if (!token.isNullOrEmpty()) {
                request.addHeader("Authorization", "Bearer $token") // Добавляем JWT в заголовок
            }

            chain.proceed(request.build())
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Логирование запросов
            .addInterceptor(authInterceptor) // Добавление токена
            .build()
    }

    // Функция для создания Retrofit API
    fun createApiService(context: Context): APIService.ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createClient(context)) // Используем HTTP-клиент с токеном
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService.ApiService::class.java)
    }
}
