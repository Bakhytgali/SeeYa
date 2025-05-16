package com.example.seeya.data.repository

import android.content.Context
import com.example.seeya.data.api.RetrofitClient
import com.example.seeya.data.model.SearchUser
import com.example.seeya.data.model.User
import com.example.seeya.utils.TokenManager
import retrofit2.Response

class SearchRepository(private val context: Context) {
    private val api = RetrofitClient.createApiService(context)

    sealed class SearchResult {
        data class Success(val users: List<SearchUser>) : SearchResult()
        data class Error(val message: String, val code: Int? = null) : SearchResult()
    }

    suspend fun searchUser(value: String): SearchResult {
        return try {
            val response = api.searchUser(value)
            if (response.isSuccessful) {
                SearchResult.Success(response.body()?.items ?: emptyList())
            } else {
                SearchResult.Error(
                    message = response.message(),
                    code = response.code()
                )
            }
        } catch (e: Exception) {
            SearchResult.Error(
                message = e.message ?: "Network error",
                code = null
            )
        }
    }
}