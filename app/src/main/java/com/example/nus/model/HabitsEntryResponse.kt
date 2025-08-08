package com.example.nus.model

import com.google.gson.annotations.SerializedName

data class HabitsEntryResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("sleep")
    val sleep: Double,
    @SerializedName("water")
    val water: Double,
    @SerializedName("workHours")
    val workHours: Double,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("archived")
    val archived: Boolean = false
)
