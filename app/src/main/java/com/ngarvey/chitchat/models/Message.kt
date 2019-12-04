package com.ngarvey.chitchat.models

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("_id")
    val id: String,
    val client: String,
    val date: String,
    val likes: Int,
    val dislikes: Int,
    val ip: String,
    @SerializedName("loc")
    val location: List<Double?>,
    val message: String
)