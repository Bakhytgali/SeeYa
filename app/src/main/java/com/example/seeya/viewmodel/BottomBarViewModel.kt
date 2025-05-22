package com.example.seeya.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class BottomBarViewModel: ViewModel() {
    var activePage by mutableStateOf("home")
        private set

    fun onActivePageChange(newValue: String) {
        activePage = newValue
    }
}