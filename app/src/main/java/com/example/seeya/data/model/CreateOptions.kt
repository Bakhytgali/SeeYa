package com.example.seeya.data.model

import androidx.annotation.DrawableRes
import com.example.seeya.R

enum class CreateOptions(val option: String, val optionDescription: String, @DrawableRes val icon: Int) {
    EVENT("Event", "Plan a one-time meetup or activity for others to join and enjoy.", R.drawable.event_icon),
    CLUB("Club", "Create a space for like-minded people to connect and grow together.", R.drawable.meetups_icon)
}