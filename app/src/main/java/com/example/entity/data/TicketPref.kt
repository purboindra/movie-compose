package com.example.entity.data

import kotlinx.serialization.Serializable

@Serializable
data class TicketPref(
    val id: String,
    val date: Long
)
