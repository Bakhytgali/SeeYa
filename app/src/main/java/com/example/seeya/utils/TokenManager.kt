package com.example.seeya.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.seeya.data.model.User
import com.google.gson.Gson

object TokenManager {
    private const val PREFS_NAME = "auth_prefs"
    private const val TOKEN_KEY = "jwt_token"
    private const val USER_KEY = "user_data"

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

    // Сохраняем объект `User`
    fun saveUser(context: Context, user: User) {
        val userJson = Gson().toJson(user) // Конвертируем объект в JSON
        getPrefs(context).edit().putString(USER_KEY, userJson).apply()
    }

    // Получаем объект `User`
    fun getUser(context: Context): User? {
        val userJson = getPrefs(context).getString(USER_KEY, null)
        return userJson?.let { Gson().fromJson(it, User::class.java) }
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