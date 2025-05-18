package com.example.seeya.data.model

enum class ClubTypes(val type: String, val description: String) {
    OPEN("Open", "Anyone can join this Club without an invitation or approval."),
    CLOSED("Closed", "Only invited or approved members can join this club.")
}