package com.example.nus.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("showEmotion")
    val showEmotion: Boolean
)
