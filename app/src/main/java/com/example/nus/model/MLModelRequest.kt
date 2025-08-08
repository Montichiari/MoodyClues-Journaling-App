package com.example.nus.model

import com.google.gson.annotations.SerializedName

data class MLModelRequest(
    @SerializedName("text")
    val text: String
)
