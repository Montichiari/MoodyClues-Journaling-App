package com.example.nus.model

import com.google.gson.annotations.SerializedName

data class JournalEntryRequest(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("mood")
    val mood: Int,
    @SerializedName("entryTitle")
    val entryTitle: String,
    @SerializedName("entryText")
    val entryText: String,
    @SerializedName("emotions")
    val emotions: List<String>
)
