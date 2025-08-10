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

// Response model for API - matches backend JournalUser
data class JournalUserResponse(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val showEmotion: Boolean = false,
    val archived: Boolean = false
) {
    fun toClient(): Client {
        return Client(
            clientId = id,
            firstName = firstName,
            lastName = lastName,
            email = email,
            linkedDate = LocalDateTime.now() // 暂时使用当前时间，后续可能需要从其他地方获取链接时间
        )
    }
}

// 保留原有的ClientResponse以兼容现有代码
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
