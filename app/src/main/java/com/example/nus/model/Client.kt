package com.example.nus.model

import java.time.LocalDateTime

data class Client(
    val clientId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val linkedDate: LocalDateTime
) {
    val fullName: String
        get() = if (lastName.isNotBlank()) "$firstName $lastName" else firstName
    
    val displayName: String
        get() = fullName.ifBlank { email.substringBefore("@") }
}

// Response model for API
data class ClientResponse(
    val clientId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val linkedDate: String // ISO date string from API
) {
    fun toClient(): Client {
        return Client(
            clientId = clientId,
            firstName = firstName,
            lastName = lastName,
            email = email,
            linkedDate = LocalDateTime.parse(linkedDate.replace(" ", "T"))
        )
    }
}
