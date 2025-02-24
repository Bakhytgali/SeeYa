package com.example.seeya.viewmodel.event

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.seeya.data.model.CreateEventRequest
import com.example.seeya.data.model.Creator
import com.example.seeya.data.model.Event
import com.example.seeya.data.repository.EventRepository
import kotlinx.coroutines.launch
import java.util.Date

class EventViewModel(application: Application, private val repository: EventRepository) :
    AndroidViewModel(application) {

    fun createEvent(
        name: String,
        description: String,
        category: String,
        eventPicture: String?,
        isClosed: Boolean,
        location: String,
        startDate: Date,
        creator: Creator,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val newEvent = CreateEventRequest(
                name = name,
                description = description,
                category = category,
                eventPicture = eventPicture,
                isOpen = isClosed,
                creator = creator,
                location = location,
                startDate = startDate,
            )

            val response = repository.createEvent(newEvent)

            if (response?.isSuccessful == true) {
                onSuccess()
            } else {
                onError("Failed to create event: ${response?.message() ?: "Unknown error"}")
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

            if(response?.isSuccessful == true) {
                response.body()?.let { events ->
                    onSuccess(events)
                } ?: onError("Empty response from the server")
            } else {
                onError("Failed to fetch your events: ${response?.message()  ?: "Unknown Error"}")
            }
        }
    }

    fun getEvent(
        eventId: String,
        onSuccess: (Event) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val response = repository.getEvent(eventId)

            if(response?.isSuccessful == true) {
                response.body()?.let {event ->
                    onSuccess(event)
                } ?: onError("Empty response from the server")
            } else {
                onError("Failed to fetch an event!: ${response?.message() ?: "Unknown Error"}")
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

            if(response?.isSuccessful == true) {
                onSuccess()
            } else {
                onError("Failed to join the event!: ${response?.message() ?: "Unknown Error"}")
            }
        }
    }
}
