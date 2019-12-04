package com.ngarvey.chitchat.api

import com.ngarvey.chitchat.models.Message

data class MessageResponse(
    val count: Int,
    val date: String,
    val messages: List<Message>
)