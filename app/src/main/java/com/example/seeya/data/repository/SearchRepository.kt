package com.example.seeya.data.repository

import android.content.Context
import com.example.seeya.data.api.RetrofitClient
import com.example.seeya.data.model.Event
import com.example.seeya.data.model.SearchUser
import com.example.seeya.data.model.User
import retrofit2.Response

class SearchRepository(private val context: Context) {
    private val api = RetrofitClient.createApiService(context)

    sealed class SearchUserResult {
        data class Success(val users: List<SearchUser>) : SearchUserResult()
        data class Error(val message: String, val code: Int? = null) : SearchUserResult()
    }

    sealed class GetUserResult {
        data class Success(val user: User?): GetUserResult()
        data class Error(val message: String, val code: Int? = null): GetUserResult()
    }

    suspend fun searchUser(value: String): SearchUserResult {
        return try {
            val response = api.searchUser(value)
            if (response.isSuccessful) {
                SearchUserResult.Success(response.body()?.items ?: emptyList())
            } else {
                SearchUserResult.Error(
                    message = response.message(),
                    code = response.code()
                )
            }
        } catch (e: Exception) {
            SearchUserResult.Error(
                message = e.message ?: "Network error",
                code = null
            )
        }
    }

    suspend fun searchEvents(value: String): List<Event> {
        return try {
            val response = api.searchEvents(value)
            if (response.isSuccessful) {
                response.body()?.items ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getUserById(userId: String): GetUserResult {
        return try {
            val response = api.getUserById(userId)
            if(response.isSuccessful) {
                GetUserResult.Success(user = response.body())
            } else {
                GetUserResult.Error(
                    message = response.message(),
                    code = response.code()
                )
            }
        } catch (e: Exception) {
            GetUserResult.Error(
                message = e.message ?: "Network Error",
                code = null
            )
        }
    }
}