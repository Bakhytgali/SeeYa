package com.example.seeya.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.seeya.R

enum class EventCategory(val title: String, @StringRes val subtitleResId: Int, @DrawableRes val iconId: Int) {
    MEETUPS("Meetups", R.string.event_category_meetup_subtitle, R.drawable.meetups_icon),
    LIVE_SHOW("Live / Shows", R.string.event_category_live_show_subtitle, R.drawable.live_shows_icon),
    ONLINE("Online", R.string.event_category_online_subtitle, R.drawable.online_icon),
    NETWORKING("Networking", R.string.event_category_networking_subtitle, R.drawable.networking_icon)
}
