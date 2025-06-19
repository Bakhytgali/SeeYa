package com.example.seeya.viewmodel.event

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.Telephony.Mms.Part
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.seeya.data.model.CreateEventRequest
import com.example.seeya.data.model.Creator
import com.example.seeya.data.model.Event
import com.example.seeya.data.model.EventApplication
import com.example.seeya.data.model.Participant
import com.example.seeya.data.model.PostModel
import com.example.seeya.data.model.PostRequestModel
import com.example.seeya.data.model.QrDataModel
import com.example.seeya.data.model.UpdateEventRequest
import com.example.seeya.data.repository.EventRepository
import com.example.seeya.utils.TokenManager
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EventViewModel(application: Application, private val repository: EventRepository) :
    AndroidViewModel(application) {

    private val user = TokenManager.getUser(getApplication())

    var event: Event? by mutableStateOf(null)
        private set

    private fun loadEvent(newEvent: Event) {
        event = newEvent
    }

    var eventTitle by mutableStateOf("")
        private set

    var eventCategory by mutableStateOf("")
        private set

    var eventTags by mutableStateOf("")
        private set

    var eventLocation by mutableStateOf("")
        private set

    var eventDescription by mutableStateOf("")
        private set

    var eventPicture by mutableStateOf("")
        private set

    var eventPictureUri: Uri? by mutableStateOf(null)

    var eventTypeValue by mutableStateOf("Open")
        private set
    var eventTypeIsOpen by mutableStateOf(true)
        private set

    var eventStartDate: LocalDateTime? by mutableStateOf(LocalDateTime.now())
        private set

    var eventCoordinates: String by mutableStateOf("")
        private set

    fun onEventCoordinatesChange(newValue: String) {
        eventCoordinates = newValue
    }

    private val _isParticipating = MutableStateFlow(false)
    val isParticipating: StateFlow<Boolean> = _isParticipating.asStateFlow()

    var eventInfoModalOpen by mutableStateOf(false)
        private set

    fun onEventInfoModalOpenChange(newValue: Boolean) {
        eventInfoModalOpen = newValue
    }

    fun onSetIsParticipating(newValue: Boolean) {
        _isParticipating.value = newValue
    }

    fun checkIfParticipating(userId: String) {
        _isParticipating.value = event!!.participants.any {
            it == userId
        }
    }

    private var _eventIsLoading = MutableStateFlow(false)
    val eventIsLoading: StateFlow<Boolean> = _eventIsLoading.asStateFlow()

    var isParticipateModalOpen by mutableStateOf(false)
        private set

    fun onIsParticipateModalOpen(newValue: Boolean) {
        isParticipateModalOpen = newValue
    }

    var alreadyParticipateModalOpen by mutableStateOf(false)
        private set

    fun onAlreadyParticipateModalOpen(newValue: Boolean) {
        alreadyParticipateModalOpen = newValue
    }

    var sentParticipateModalOpen by mutableStateOf(false)
        private set

    fun sentParticipateModalOpen(newValue: Boolean) {
        sentParticipateModalOpen = newValue
    }

    fun onEventTitleChange(newValue: String) {
        eventTitle = newValue
    }

    fun onEventCategoryChange(newValue: String) {
        eventCategory = newValue
    }

    fun onEventLocationChange(newValue: String) {
        eventLocation = newValue
    }

    fun onEventDescriptionChange(newValue: String) {
        eventDescription = newValue
    }

    private fun setNewEventPicture(pictureUrl: String?) {
        eventPicture = pictureUrl ?: ""
    }

    fun onNewEventCategory(newValue: String) {
        eventCategory = newValue
    }

    fun setEventType(newValue: String) {
        eventTypeValue = newValue
        eventTypeIsOpen = newValue == "Open"
    }

    fun setNewEventStartDate(dateTime: LocalDateTime) {
        eventStartDate = dateTime
    }

    fun onNewEventTags(newValue: String) {
        eventTags = newValue
    }

    fun handleImageUri(uri: Uri) {
        eventPictureUri = uri
    }

    fun uploadImageToCloudinary(onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                Log.d("Cloudinary", "Start uploading image...")
                val imageUrl = repository.uploadImageToCloudinary(eventPictureUri!!)
                Log.d("Cloudinary", "Upload success, image URL: $imageUrl")
                eventPicture = imageUrl

                createEvent(
                    onSuccess = {
                        onResult(true, "Success!")
                        clearEntries()
                    },
                    onError = {
                        onResult(false, "Error creating event: $it")
                        clearEntries()
                    }
                )
            } catch (e: Exception) {
                Log.e("Cloudinary", "Upload failed: ${e.message}", e)
                onResult(false, "Upload exception: ${e.message}")
                clearEntries()
            }
        }
    }

    private fun createEvent(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (user != null) {
            val userCreator = Creator(
                id = user.id!!,
                name = user.name,
                surname = user.surname,
                username = user.username,
                rating = null
            )

            Log.d("Event Creator", "${event?.startDate}")

            viewModelScope.launch {
                val newEvent = CreateEventRequest(
                    name = eventTitle,
                    description = eventDescription,
                    category = eventCategory,
                    eventPicture = eventPicture,
                    isOpen = eventTypeIsOpen,
                    creator = userCreator,
                    location = eventLocation,
                    locationCoordinates = eventCoordinates,
                    startDate = eventStartDate.toString(),
                    eventTags = eventTags,
                )

                Log.d("Event View Model", eventCoordinates)

                val response = repository.createEvent(newEvent)

                if (response?.isSuccessful == true) {
                    onSuccess()
                    clearEntries()
                } else {
                    onError("Failed to create event: ${response?.message() ?: "Unknown error"}")
                    clearEntries()
                }
            }
        }
    }

    fun getAllEvents(
        onSuccess: (List<Event>) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val response = repository.getAllEvents()
            if (response?.isSuccessful == true) {
                response.body()?.let { events ->
                    onSuccess(events)
                } ?: onError("Empty response from server")
            } else {
                onError("Failed to fetch events: ${response?.message() ?: "Unknown Error"}")
            }
        }
    }

    fun getMyEvents(
        onSuccess: (List<Event>) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val response = repository.getMyEvents()

            if (response?.isSuccessful == true) {
                response.body()?.let { events ->
                    onSuccess(events)
                } ?: onError("Empty response from the server")
            } else {
                onError("Failed to fetch your events: ${response?.message() ?: "Unknown Error"}")
            }
        }
    }

    fun getEvent(
        eventId: String,
        onSuccess: (Event) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _eventIsLoading.value = true
            try {
                val response = repository.getEvent(eventId)

                if (response?.isSuccessful == true) {
                    response.body()?.let { event ->
                        loadEvent(event)
                        onSuccess(event)
                    } ?: run {
                        onError("Empty response from the server")
                    }
                } else {
                    onError("Failed to fetch an event!: ${response?.message() ?: "Unknown Error"}")
                }
            } catch (e: Exception) {
                onError("Network error: ${e.message}")
            } finally {
                _eventIsLoading.value = false
            }
        }
    }

    fun joinEvent(
        eventId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val response = repository.joinEvent(eventId = eventId)

            if (response?.isSuccessful == true) {
                onSuccess()
            } else {
                onError("Failed to join the event!: ${response?.message() ?: "Unknown Error"}")
            }
        }
    }

    private val _attendanceList = MutableStateFlow<List<Participant>>(emptyList())
    val attendanceList: StateFlow<List<Participant>> = _attendanceList.asStateFlow()

    private val _loadingAttendance = MutableStateFlow(false)
    val loadingAttendance: StateFlow<Boolean> = _loadingAttendance

    fun checkAttendance(qrData: QrDataModel, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repository.checkAttendance(qrData)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Error code: ${response.code()}")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error")
            }
        }
    }

    private val _applications = MutableStateFlow<List<EventApplication>>(emptyList())
    val applications: StateFlow<List<EventApplication>> = _applications

    private val _loadingApplications = MutableStateFlow(false)
    val loadingApplications: StateFlow<Boolean> = _loadingApplications

    fun loadEventApplications(eventId: String) {
        viewModelScope.launch {
            _loadingApplications.value = true
            val response = repository.getEventApplications(eventId)
            if (response.isSuccessful) {
                _applications.value = response.body() ?: emptyList()
            } else {
                _applications.value = emptyList()
                Log.e(
                    "EventViewModel",
                    "Failed to load applications: ${response.errorBody()?.string()}"
                )
            }
            _loadingApplications.value = false
        }
    }

    fun acceptApplication(applicationId: String) {
        viewModelScope.launch {
            _loadingApplications.value = true
            val result = repository.acceptApplication(applicationId)
            result?.let {
                _applications.value = it
            }
            _loadingApplications.value = false
        }
    }

    fun rejectApplication(applicationId: String) {
        viewModelScope.launch {
            _loadingApplications.value = true
            val result = repository.rejectApplication(applicationId)
            result?.let {
                _applications.value = it
            }
            _loadingApplications.value = false
        }
    }

    private val _visitedEvents = MutableStateFlow<List<Event>>(emptyList())
    val visitedEvents: StateFlow<List<Event>> = _visitedEvents

    private val _loadingVisitedEvents = MutableStateFlow(false)
    val loadingVisitedEvents: StateFlow<Boolean> = _loadingVisitedEvents

    fun getVisitedEvents() {
        viewModelScope.launch {
            _loadingVisitedEvents.value = true
            try {
                val result = repository.getVisitedEvents()
                _visitedEvents.value = result.body() ?: emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
                _visitedEvents.value = emptyList()
            } finally {
                _loadingVisitedEvents.value = false
            }
        }
    }

    fun rateEvent(eventId: String, rating: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repository.rateEvent(eventId, rating)
                onResult(response.isSuccessful)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }
    }

    var addPostStatus by mutableStateOf<Int?>(null)
        private set

    private suspend fun uploadMediaToCloudinary(uri: Uri): String {
        return repository.uploadImageToCloudinary(uri)
    }

    fun addPostMediaToCloudinary(
        media: List<Uri>,
        header: String,
        content: String,
        eventId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val urls = media.map { uri ->
                    uploadMediaToCloudinary(uri)
                }

                addPost(
                    header = header,
                    content = content,
                    media = urls,
                    eventId = eventId,
                    onSuccess = onSuccess,
                    onError = { onError("Ошибка при создании поста") }
                )
            } catch (e: Exception) {
                onError("Ошибка загрузки медиа: ${e.message}")
            }
        }
    }

    private fun addPost(
        header: String,
        content: String,
        media: List<String>,
        eventId: String,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        val date = LocalDateTime.now().format(formatter)
        viewModelScope.launch {
            val post = PostRequestModel(header, content, media, date)
            val resultCode = repository.addPost(eventId, post)

            if (resultCode == 200) {
                onSuccess()
            } else {
                onError()
            }
        }
    }

    fun resetAddPostStatus() {
        addPostStatus = null
    }

    var post by mutableStateOf<PostModel?>(null)
        private set

    var getPostLoading by mutableStateOf(false)
        private set

    var getPostErrorMessage by mutableStateOf<String?>(null)
        private set

    fun fetchPost(postId: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            getPostLoading = true
            getPostErrorMessage = null

            try {
                val response = repository.getPost(postId)
                if (response.isSuccessful) {
                    post = response.body()
                    onResult(true, null)
                } else {
                    getPostErrorMessage = "Ошибка ${response.code()}: ${response.message()}"
                    onResult(false, response.message())
                }
            } catch (e: Exception) {
                getPostErrorMessage = "Ошибка получения поста: ${e.message}"
                e.printStackTrace()
                onResult(false, e.message)
            } finally {
                getPostLoading = false
                onResult(false, "Unknown Error")
            }
        }
    }

    var posts by mutableStateOf<List<PostModel>>(emptyList())
        private set

    var fetchPostsLoading by mutableStateOf(false)
        private set

    var fetchPostsErrorMessage by mutableStateOf<String?>(null)
        private set

    fun fetchPosts(eventId: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            fetchPostsLoading = true
            fetchPostsErrorMessage = null

            try {
                val response = repository.fetchPosts(eventId)
                if (response.isSuccessful) {
                    posts = response.body().orEmpty()
                    onResult(true, null)
                } else {
                    fetchPostsErrorMessage = "Ошибка ${response.code()}: ${response.message()}"
                    onResult(false, response.message())
                }
            } catch (e: Exception) {
                fetchPostsErrorMessage = "Ошибка получения постов: ${e.message}"
                e.printStackTrace()
                onResult(false, e.message)
            } finally {
                fetchPostsLoading = false
            }
        }
    }

    var likeErrorMessage by mutableStateOf<String?>(null)
        private set

    suspend fun likePost(postId: String): List<String>? {
        return try {
            val response = repository.likePost(postId)
            if (response.isSuccessful) {
                response.body()
            } else {
                likeErrorMessage = "Ошибка ${response.code()}: ${response.message()}"
                null
            }
        } catch (e: Exception) {
            likeErrorMessage = "Ошибка лайка: ${e.message}"
            null
        }
    }

    suspend fun unlikePost(postId: String): List<String>? {
        return try {
            val response = repository.unlikePost(postId)
            if (response.isSuccessful) {
                response.body()
            } else {
                likeErrorMessage = "Ошибка ${response.code()}: ${response.message()}"
                null
            }
        } catch (e: Exception) {
            likeErrorMessage = "Ошибка удаления лайка: ${e.message}"
            null
        }
    }

    fun updateEventInfo(
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val eventId = event?.eventId
                if (eventId == null) {
                    onResult(false, "Event ID is null")
                    return@launch
                }

                val newImageUrl = if (eventPictureUri != null) {
                    try {
                        repository.uploadImageToCloudinary(eventPictureUri!!).also {
                            eventPicture = it
                        }
                    } catch (e: Exception) {
                        onResult(false, "Image upload failed: ${e.message}")
                        return@launch
                    }
                } else {
                    event?.eventPicture
                }

                val request = UpdateEventRequest(
                    newTitle = eventTitle,
                    newDescription = eventDescription,
                    newLocation = eventLocation,
                    newCoordinates = eventCoordinates,
                    newPicture = newImageUrl // ВАЖНО
                )

                val response = repository.updateEvent(
                    eventId = eventId,
                    request = request
                )

                if (response.isSuccessful) {
                    event = event?.copy(
                        name = eventTitle,
                        description = eventDescription,
                        location = eventLocation,
                        eventPicture = newImageUrl,
                        locationCoordinates = eventCoordinates
                    )
                    onResult(true, "Event updated successfully")
                } else {
                    onResult(false, "Update failed: ${response.message()}")
                }

            } catch (e: Exception) {
                onResult(false, "Exception: ${e.message}")
            }
        }
    }

    private val _eventMedia = mutableStateOf<List<String>>(emptyList())
    val eventMedia: List<String> get() = _eventMedia.value

    fun fetchEventMedia(eventId: String, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            try {
                val media = repository.getEventMedia(eventId)
                _eventMedia.value = media
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error")
            }
        }
    }

    fun clearEntries() {
        eventTitle = ""
        eventDescription = ""
        eventLocation = ""
        eventCoordinates = ""
        eventTags = ""
        eventCategory = ""
        setEventType("Open")
        eventPicture = ""
        eventPictureUri = null
    }
}
