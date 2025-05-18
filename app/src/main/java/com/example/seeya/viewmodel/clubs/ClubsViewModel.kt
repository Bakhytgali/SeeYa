package com.example.seeya.viewmodel.clubs

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.seeya.data.model.Club
import com.example.seeya.data.model.ClubTypes
import com.example.seeya.data.model.SearchUser
import com.example.seeya.data.repository.ClubsRepository
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class ClubsViewModel(application: Application, private val repository: ClubsRepository): AndroidViewModel(application) {
    var club: Club? by mutableStateOf(null)
        private set

    var myClubs: ClubsState by mutableStateOf(ClubsState.Idle)
        private set

    var createClubTitle by mutableStateOf("")
        private set
    fun onNewClubTitleChange(newValue: String) {
        createClubTitle = newValue
    }

    var createClubCategory by mutableStateOf("")
        private set
    fun onNewClubCategoryChange(newValue: String) {
        createClubCategory = newValue
    }

    var createNewClubIsOpen by mutableStateOf(true)
        private set

    var createNewClubType by mutableStateOf(ClubTypes.OPEN.type)
        private set

    fun onNewClubIsOpenChange(clubType: ClubTypes) {
        createNewClubIsOpen = clubType.type == ClubTypes.OPEN.type
        createNewClubType = clubType.type
    }

    var createNewClubTags by mutableStateOf("")
        private set
    fun onNewClubTagsChange(newValue: String) {
        createNewClubTags = newValue
    }

    var createNewClubDescription by mutableStateOf("")
        private set
    fun onNewClubDescriptionChange(newValue: String) {
        createNewClubDescription = newValue
    }

    var imageBitmap: Bitmap? by mutableStateOf(null)
        private set

    var createNewClubPicture by mutableStateOf("")
        private set

    private fun onNewClubPictureChange(clubPicture: String?) {
        if(!clubPicture.isNullOrEmpty()) {
            createNewClubPicture = clubPicture
        }
    }

    fun handleImageUri(uri: Uri) {
        val contentResolver = getApplication<Application>().contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()

        inputStream?.close()

        if(bytes != null) {
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            imageBitmap = bitmap
            onNewClubPictureChange(encodeToBase64(bitmap))
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

    private fun encodeToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun createNewClub(
        onSuccess: () -> Unit,
        onError: () -> Unit,
    ) {

    }

    fun getMyClubs() {
        viewModelScope.launch{
            myClubs = ClubsState.Loading
            myClubs = when(val result = repository.getMyEvents()) {
                is ClubsRepository.ClubsResult.Success -> {
                    if(result.clubs.isNotEmpty()) {
                        ClubsState.Success(result.clubs)
                    } else {
                        ClubsState.Empty
                    }
                }

                is ClubsRepository.ClubsResult.Error -> {
                    ClubsState.Error(
                        message = result.message,
                        code = result.code
                    )
                }
            }
        }
    }

    fun clearEntries() {
        createClubTitle = ""
        createClubCategory = ""
        createNewClubTags = ""
        createNewClubIsOpen = true
        createNewClubPicture = ""
        createNewClubDescription = ""
    }
}

sealed class ClubsState {
    data object Idle : ClubsState()
    data object Loading : ClubsState()
    data object Empty : ClubsState()
    data class Success(val clubs: List<Club>) : ClubsState()
    data class Error(val message: String, val code: Int? = null) : ClubsState()
}