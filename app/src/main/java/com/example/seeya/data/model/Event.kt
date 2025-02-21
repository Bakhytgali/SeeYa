package com.example.seeya.data.model

import java.util.Date

data class Event(
    val name: String,
    val description: String,
    val category: String,
    val eventPicture: String,
    val isClosed: Boolean,
    val creatorId: String,
    val participants: List<String>,
    val location: String,
    val startDate: Date,
    val endDate: Date,
    val createdAt: Date
)


//const EventSchema = new mongoose.Schema({
//  name: { type: String, required: true },
//  description: { type: String },
//  category: { type: String },
//  picture: { type: Buffer },
//  isClosed: {type: Boolean, default: false},
//  creatorId: { type: mongoose.Schema.Types.ObjectId, ref: "User", required: true },
//  participants: [{ type: mongoose.Schema.Types.ObjectId, ref: "User" }],
//  maxParticipants: { type: Number },
//  location: { type: String },
//  startDate: { type: Date, required: true },
//  endDate: { type: Date, required: true },
//  createdAt: { type: Date, default: Date.now }
//});