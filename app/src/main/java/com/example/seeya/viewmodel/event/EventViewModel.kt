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
import androidx.lifecycle.viewModelScope
import com.example.seeya.data.model.CreateEventRequest
import com.example.seeya.data.model.Creator
import com.example.seeya.data.model.Event
import com.example.seeya.data.model.Participant
import com.example.seeya.data.model.QrDataModel
import com.example.seeya.data.repository.EventRepository
import com.example.seeya.utils.TokenManager
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

    var imageBitmap: Bitmap? by mutableStateOf(null)
        private set

    var eventTypeValue by mutableStateOf("Open")
        private set
    var eventTypeIsOpen by mutableStateOf(true)
        private set

    var eventStartDate: LocalDateTime? by mutableStateOf(LocalDateTime.now())
        private set

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
            it.id == userId
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
        val contentResolver = getApplication<Application>().contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()

        inputStream?.close()

        if(bytes != null) {
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            imageBitmap = bitmap
            setNewEventPicture(encodeToBase64(bitmap))
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

    fun createEvent(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val formatted = eventStartDate!!.format(formatter)
        if (user != null) {
            val userCreator = Creator(
                id = user.id!!,
                name = user.name,
                surname = user.surname,
                username = user.username,
                rating = null
            )

            viewModelScope.launch {
                val newEvent = CreateEventRequest(
                    name = eventTitle,
                    description = eventDescription,
                    category = eventCategory,
                    eventPicture = eventPicture,
                    isOpen = eventTypeIsOpen,
                    creator = userCreator,
                    location = eventLocation,
                    startDate = formatted,
                    eventTags = eventTags
                )

                val response = repository.createEvent(newEvent)

                if (response?.isSuccessful == true) {
                    onSuccess()
                } else {
                    onError("Failed to create event: ${response?.message() ?: "Unknown error"}")
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

    fun clearEntries() {
        eventTitle = ""
        eventDescription = ""
        eventLocation = ""
        eventTags = ""
        eventCategory = ""
        setEventType("Open")
        eventPicture = ""
        imageBitmap = null
    }
}
