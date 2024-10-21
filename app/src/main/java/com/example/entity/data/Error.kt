package com.example.entity.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Error(
    val success: Boolean,

    @SerialName("status_code")
    val statusCode: Int,

    @SerialName("status_message")
    val statusMessage: String,

    )