package com.example.seeya.data.model

import androidx.annotation.StringRes
import com.example.seeya.R

enum class EventType(val title: String, @StringRes val description: Int) {
    OPEN("Open", R.string.event_type_open_description),
    CLOSED("Closed", R.string.event_type_closed_description)
}