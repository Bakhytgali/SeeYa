package com.example.seeya.viewmodel.clubs

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seeya.data.repository.ClubsRepository
import com.example.seeya.viewmodel.auth.AuthViewModel

class ClubsViewModelFactory(
    private val application: Application,
    private val repository: ClubsRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClubsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ClubsViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}