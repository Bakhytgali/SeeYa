package com.example.seeya.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.seeya.data.model.User
import com.example.seeya.data.repository.AuthRepository
import com.example.seeya.utils.TokenManager
import kotlinx.coroutines.launch

class AuthViewModel(application: Application, private val repository: AuthRepository) : AndroidViewModel(application) {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> = _token

    init {
        loadUserFromPrefs() // Загружаем сохранённого юзера
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val response = repository.loginUser(email, password)
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    val context = getApplication<Application>().applicationContext
                    TokenManager.saveToken(context, authResponse.token)
                    TokenManager.saveUser(context, authResponse.user)

                    _user.postValue(authResponse.user) // Обновляем UI
                    _token.postValue(authResponse.token)
                }
            }
        }
    }

    private fun loadUserFromPrefs() {
        val context = getApplication<Application>().applicationContext
        _user.postValue(TokenManager.getUser(context))
        _token.postValue(TokenManager.getToken(context))
    }

    fun logout() {
        val context = getApplication<Application>().applicationContext
        TokenManager.logout(context)
        _user.postValue(null)
        _token.postValue(null)
    }
}
