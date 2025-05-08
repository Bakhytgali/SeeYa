package com.example.seeya.viewmodel.auth

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.seeya.data.model.SignInRequest
import com.example.seeya.data.model.User
import com.example.seeya.data.repository.AuthRepository
import com.example.seeya.utils.TokenManager
import kotlinx.coroutines.launch

class AuthViewModel(application: Application, private val repository: AuthRepository) :
    AndroidViewModel(application) {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> = _token

    var loginText by mutableStateOf("")
        private set

    var loginPassword by mutableStateOf("")
        private set

    var isError by mutableStateOf(false)
        private set

    fun onLoginTextChange(newValue: String) {
        loginText = newValue
    }

    fun onLoginPasswordChange(newValue: String) {
        loginPassword = newValue
    }

    fun setErrorValue(newValue: Boolean) {
        isError = newValue
    }

    var registerEmail by mutableStateOf("")
        private set

    var registerPassword by mutableStateOf("")
        private set

    var registerName by mutableStateOf("")
        private set

    var registerSurname by mutableStateOf("")
        private set

    var registerUsername by mutableStateOf("")
        private set

    fun onRegisterEmailChange(newValue: String) {
        registerEmail = newValue
    }

    fun onRegisterPasswordChange(newValue: String) {
        registerPassword = newValue
    }

    fun onRegisterNameChange(newValue: String) {
        registerName = newValue
    }

    fun onRegisterSurnameChange(newValue: String) {
        registerSurname = newValue
    }

    fun onRegisterUsernameChange(newValue: String) {
        registerUsername = newValue
    }

    var registerDialogIsOpen by mutableStateOf(false)
        private set

    fun setDialogOpen(newValue: Boolean) {
        registerDialogIsOpen = newValue
    }

    init {
        loadUserFromPrefs()
    }

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val response = repository.loginUser(loginText, loginPassword)
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    val context = getApplication<Application>().applicationContext
                    TokenManager.saveToken(context, authResponse.token)
                    TokenManager.saveUser(context, authResponse.user)

                    _user.postValue(authResponse.user)
                    _token.postValue(authResponse.token)

                    onSuccess()
                }
            }
        }
    }

    fun register(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val response = repository.registerUser(
                SignInRequest(
                    name = registerName,
                    surname = registerSurname,
                    username = registerUsername,
                    email = registerEmail,
                    password = registerPassword
                )
            )

            if (!response.isNullOrEmpty()) {
                Log.d("AuthViewModel", "Registration successful: $response")
                onSuccess()
            } else {
                Log.e("AuthViewModel", "Registration Failed!")
                onError("Registration failed. Please try again.")
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
