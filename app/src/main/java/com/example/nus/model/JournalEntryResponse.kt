package com.example.nus.model

import com.google.gson.annotations.SerializedName

data class JournalEntryResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("mood")
    val mood: Int,
    @SerializedName("entryTitle")
    val entryTitle: String,
    @SerializedName("entryText")
    val entryText: String,
    @SerializedName("emotions")
    val emotions: List<String>,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("archived")
    val archived: Boolean = false
)
