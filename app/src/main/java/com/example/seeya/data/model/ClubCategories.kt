package com.example.seeya.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.seeya.R

enum class ClubCategories(val title: String, @StringRes val description: Int, @DrawableRes val icon: Int) {
    COMMUNITY("Community", R.string.club_category_subtitle_community, R.drawable.meetups_icon),
    VOLUNTEERING("Volunteering", R.string.club_category_subtitle_volunteering, R.drawable.volunteering_icon),
    NETWORKING("Networking", R.string.club_category_subtitle_networking, R.drawable.online_icon),
    PROMOTION("Promotion", R.string.club_category_subtitle_promotion, R.drawable.promotion_icon);
}