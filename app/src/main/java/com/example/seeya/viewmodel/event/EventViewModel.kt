package com.example.seeya.viewmodel.event

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.seeya.data.model.CreateEventRequest
import com.example.seeya.data.model.Creator
import com.example.seeya.data.model.Event
import com.example.seeya.data.model.GetAllEventsResponse
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
                response.body()?.let {
                    onSuccess(
                        it.events
                    )
                }
            } else {
                onError("Failed to fetch the events!: ${response?.message() ?: "Unknown Error"}")
            }
        }
    }
}
