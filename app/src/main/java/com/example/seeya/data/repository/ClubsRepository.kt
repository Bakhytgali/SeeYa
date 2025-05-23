package com.example.seeya.data.repository

import android.content.Context
import com.example.seeya.data.api.RetrofitClient
import com.example.seeya.data.model.Club
import com.example.seeya.data.model.CreateClubRequest
import com.example.seeya.utils.TokenManager

class ClubsRepository(private val context: Context) {
    private val api = RetrofitClient.createApiService(context)

    sealed class ClubsResult {
        data class Success(val clubs: List<Club>) : ClubsResult()
        data class Error(val message: String, val code: Int? = null) : ClubsResult()
    }

    private val token = TokenManager.getToken(context)

    suspend fun getMyEvents(): ClubsResult {

        return try {
            if (token.isNullOrEmpty()) {
                throw Exception("No auth token found.")
            }
            val response = api.getMyClubs(token)
            if(response.isSuccessful) {
                ClubsResult.Success(response.body() ?: emptyList())
            } else {
                ClubsResult.Error(
                    message = response.message(),
                    code = response.code()
                )
            }
        } catch (e: Exception) {
            ClubsResult.Error(
                message = e.message ?: "Network Error",
                code = null
            )
        }
    }

    suspend fun createClub(club: CreateClubRequest): String {
        if (token.isNullOrEmpty()) {
            throw Exception("Not authenticated")
        }
        return api.createClub(club = club, token = "Bearer $token")
    }

    suspend fun getClubById(clubId: String): Club? {
        return try {
            val response = api.getClubById(clubId)
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