package com.example.nus.model


import java.time.LocalDateTime

data class LinkRequest(
    val id: String,
    val counsellorUser: CounsellorUser,      // or CounsellorUser if you have a subclass
    val journalUser: JournalUser,         // or JournalUser if you have a subclass
    val requestedAt: LocalDateTime,
    val status: LinkStatus
)

// Define an enum class for status
enum class LinkStatus {
    PENDING,
    APPROVED,
    REJECTED
}
