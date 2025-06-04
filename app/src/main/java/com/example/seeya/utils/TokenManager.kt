package com.example.seeya.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.seeya.data.model.User
import com.google.gson.Gson

object TokenManager {
    private const val PREFS_NAME = "auth_prefs"
    private const val TOKEN_KEY = "jwt_token"
    private const val USER_KEY = "user_data"

    private const val FCM_TOKEN_KEY = "fcm_token"

    // Сохраняем FCM-токен
    fun saveFcmToken(context: Context, token: String) {
        getPrefs(context).edit().putString(FCM_TOKEN_KEY, token).apply()
    }

    // Получаем FCM-токен
    fun getFcmToken(context: Context): String? {
        return getPrefs(context).getString(FCM_TOKEN_KEY, null)
    }

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Сохраняем токен
    fun saveToken(context: Context, token: String) {
        getPrefs(context).edit().putString(TOKEN_KEY, token).apply()
    }

    // Получаем токен
    fun getToken(context: Context): String? {
        return getPrefs(context).getString(TOKEN_KEY, null)
    }

    // Очищаем токен
    fun clearToken(context: Context) {
        getPrefs(context).edit().remove(TOKEN_KEY).apply()
    }

    fun saveUser(context: Context, user: User) {
        val userJson = Gson().toJson(user)
        getPrefs(context).edit().putString(USER_KEY, userJson).apply()
    }

    fun getUser(context: Context): User? {
        val userJson = getPrefs(context).getString(USER_KEY, null)
        return userJson?.let {
            try {
                Gson().fromJson(it, User::class.java).also { parsedUser ->
                }
            } catch (e: Exception) {
                null
            }
        }
    }


    // Очищаем пользователя
    fun clearUser(context: Context) {
        getPrefs(context).edit().remove(USER_KEY).apply()
    }

    // Полный выход (очищаем всё)
    fun logout(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}