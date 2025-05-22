package com.example.seeya.viewmodel.auth

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.seeya.data.model.SignInRequest
import com.example.seeya.data.model.User
import com.example.seeya.data.repository.AuthRepository
import com.example.seeya.ui.theme.grayText
import com.example.seeya.utils.TokenManager
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class AuthViewModel(application: Application, private val repository: AuthRepository) :
    AndroidViewModel(application) {

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

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

                    _currentUser.postValue(authResponse.user)
                    _token.postValue(authResponse.token)

                    onSuccess()
                }
            }
        }
    }

    private fun loadUserFromPrefs() {
        val context = getApplication<Application>().applicationContext
        _currentUser.postValue(TokenManager.getUser(context))
        _token.postValue(TokenManager.getToken(context))
    }

    fun logout() {
        val context = getApplication<Application>().applicationContext
        TokenManager.logout(context)
        _currentUser.postValue(null)
        _token.postValue(null)
    }

    // Registration

    var registrationStep by mutableIntStateOf(1)
        private set

    var registerEmail by mutableStateOf("")
        private set

    var registerPassword by mutableStateOf("")
        private set

    var registerIsPasswordConfirmedIndicator by mutableStateOf("Enter your password again to confirm it")
        private set

    var registerIsPasswordConfirmedIndicatorColor by mutableStateOf(grayText)
        private set

    var registerConfirmPassword by mutableStateOf("")
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
        validateRegisterPassword()
    }

    fun onRegisterConfirmPasswordChange(newValue: String) {
        registerConfirmPassword = newValue
        validateRegisterPassword()
    }

    private fun validateRegisterPassword() {
        registerIsPasswordConfirmedIndicator = when {
            registerPassword.isBlank() || registerConfirmPassword.isBlank() -> "Enter your password again to confirm it"
            registerPassword == registerConfirmPassword -> {
                registerIsPasswordConfirmedIndicatorColor = grayText
                ""
            }

            else -> {
                registerIsPasswordConfirmedIndicatorColor = Color.Red
                "Passwords don't match"
            }
        }
    }

    fun onRegisterNameChange(newValue: String) {
        registerName = newValue
    }

    fun onRegisterSurnameChange(newValue: String) {
        registerSurname = newValue
    }

    fun onRegisterUsernameChange(newValue: String) {
        registerUsername = newValue.lowercase()
    }

    var registerDialogIsOpen by mutableStateOf(false)
        private set

    fun setDialogOpen(newValue: Boolean) {
        registerDialogIsOpen = newValue
    }

    fun onRegistrationStepChange(nextStep: Int) {
        registrationStep = nextStep

        Log.d("Register Page", "Register Step: $registrationStep")
    }

    var registerPrefs = mutableStateListOf<String>()

    fun onRegisterPrefsChange(newValue: String) {
        if(!registerPrefs.contains(newValue)) {
            registerPrefs.add(newValue)
        }
    }

    fun checkIfPrefsAdded(newValue: String): Boolean = newValue in registerPrefs

    var imageBitmap: Bitmap? by mutableStateOf(null)

    private var registerPicture: String? by mutableStateOf("")

    private fun onRegisterPictureChange(newPicture: String) {
        registerPicture = newPicture
    }

    private fun encodeToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun handleImageUri(uri: Uri) {
        val contentResolver = getApplication<Application>().contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()

        inputStream?.close()

        if(bytes != null) {
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            imageBitmap = bitmap
            onRegisterPictureChange(encodeToBase64(bitmap))
        }
    }

    fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    var verifyCodeText by mutableStateOf("")
        private set

    fun onVerifyCodeTextChange(newValue: String) {
        verifyCodeText = newValue
    }

    fun register(onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                Log.d("AuthViewModel", "Starting registration process...")
                Log.d("AuthViewModel", "Request data: " +
                        "name=${registerName}, " +
                        "surname=${registerSurname}, " +
                        "username=${registerUsername}, " +
                        "email=${registerEmail}, " +
                        "password=***, " + // Не логируем пароль напрямую
                        "interestedTags=${registerPrefs}, " +
                        "profilePicture=${registerPicture?.take(10)}...") // Логируем только начало строки base64

                val response = repository.registerUser(
                    SignInRequest(
                        name = registerName,
                        surname = registerSurname,
                        username = registerUsername,
                        email = registerEmail,
                        password = registerPassword,
                        interestedTags = registerPrefs,
                        profilePicture = registerPicture
                    )
                )

                if (!response.isNullOrEmpty()) {
                    Log.d("AuthViewModel", "Registration successful. Response: $response")
                    onResult(true, "Registration successful!")
                } else {
                    Log.e("AuthViewModel", "Registration failed: empty or null response")
                    onResult(false, "Registration failed. Please try again.")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Registration error: ${e.message}", e)
                onResult(false, "Network error: ${e.localizedMessage ?: "Unknown error"}")
            }
        }
    }


    fun verifyEmail(onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repository.verifyEmail(registerEmail)
                if (response) {
                    onResult(true, "Email verification successful")
                } else {
                    onResult(false, "Email verification failed")
                }
            } catch (e: Exception) {
                onResult(false, "Error: ${e.localizedMessage ?: "Unknown error"}")
            }
        }
    }

    fun verifyCode(onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repository.verifyCode(code = verifyCodeText, email = registerEmail)
                if(response) {
                    onResult(true, "Code Verification Successful")
                } else {
                    onResult(false, "Failed to Verify the code")
                }
            } catch (e: Exception) {
                onResult(false, e.localizedMessage ?: "Unknown Error")
            }
        }
    }

    fun clearRegisterEntries() {
        registerEmail = ""
        registerPassword = ""
        registerUsername = ""
        registerConfirmPassword = ""
        registerSurname = ""
        registerIsPasswordConfirmedIndicator = ""
        registerIsPasswordConfirmedIndicatorColor = grayText
        registerDialogIsOpen = false
        imageBitmap = null
        registerPicture = ""
        registrationStep = 1
        registerPrefs.clear()
    }

}
