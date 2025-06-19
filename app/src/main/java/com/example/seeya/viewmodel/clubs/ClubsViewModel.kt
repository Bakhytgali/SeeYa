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
import com.example.seeya.data.model.Club
import com.example.seeya.data.model.ClubTypes
import com.example.seeya.data.model.CreateClubRequest
import com.example.seeya.data.model.Creator
import com.example.seeya.data.repository.ClubsRepository
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.time.LocalDate

class ClubsViewModel(application: Application, private val repository: ClubsRepository): AndroidViewModel(application) {
    var club by mutableStateOf<Club?>(null)
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

    var isLoading by mutableStateOf(false)
    var isParticipating by mutableStateOf(false)
    var newClub by mutableStateOf<Club?>(null)

    var showJoinSuccessDialog by mutableStateOf(false)
    var showJoinErrorDialog by mutableStateOf(false)
    var showAlreadyJoinedDialog by mutableStateOf(false)
    var showClubInfoDialog by mutableStateOf(false)

    fun checkIfParticipating(userId: String) {
        viewModelScope.launch {
            isParticipating = club?.participants?.any { it == userId } ?: false
        }
    }

//
//    fun leaveClub(clubId: String) {
//        viewModelScope.launch {
//            repository.leaveClub(clubId)
//            isParticipating = false
//        }
//    }

//    fun joinClub(clubId: String, onSuccess: () -> Unit, onError: () -> Unit) {
//        viewModelScope.launch {
//            try {
//                val success = repository.joinClub(clubId)
//                if (success) {
//                    onSuccess()
//                } else {
//                    onError()
//                }
//            } catch (e: Exception) {
//                onError()
//            }
//        }
//    }

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

    fun createClub(creator: Creator, onResult: (String?, String?) -> Unit) {
        viewModelScope.launch {
            val newClub = CreateClubRequest(
                name = createClubTitle,
                isOpen = createNewClubIsOpen,
                clubTags = createNewClubTags,
                clubPicture = createNewClubPicture,
                createdAt = LocalDate.now(),
                description = createNewClubDescription,
                category = createClubCategory,
                participants = emptyList(),
                creator = creator
            )

            try {
                val clubId = repository.createClub(newClub)
                if (clubId.isNotEmpty()) {
                    onResult(clubId, null)
                } else {
                    onResult(null, "Сервер не вернул ID клуба")
                }
            } catch (e: Exception) {
                onResult(null, e.message ?: "Ошибка создания клуба")
            }
        }
    }


    fun fetchClubById(clubId: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                println("Fetching club with id: $clubId")
                club = repository.getClubById(clubId)
                println("Club fetched successfully: ${club != null}")
            } catch (e: Exception) {
                println("Error fetching club: ${e.message}")
            } finally {
                isLoading = false
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
        imageBitmap = null
    }
}

sealed class ClubsState {
    data object Idle : ClubsState()
    data object Loading : ClubsState()
    data object Empty : ClubsState()
    data class Success(val clubs: List<Club>) : ClubsState()
    data class Error(val message: String, val code: Int? = null) : ClubsState()
}