package com.example.seeya.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel

class ThemeViewModel(application: Application): AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("theme", Context.MODE_PRIVATE)

    var isDarkMode = mutableStateOf(false)
        private set

    init {
        isDarkMode.value = prefs.getBoolean("dark_mode", false)
    }

    fun onDarkModeChange() {
        isDarkMode.value = !isDarkMode.value
        prefs.edit().putBoolean("dark_mode", isDarkMode.value).apply()
    }
}