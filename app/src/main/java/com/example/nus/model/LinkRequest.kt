package com.example.nus.model

import java.time.LocalDateTime

data class LinkRequest(
    val id: String,
    val counsellorUser: CounsellorUser,
    val journalUser: JournalUser,
    val requestedAt: LocalDateTime,
    val status: LinkStatus
)

// Define an enum class for status
enum class LinkStatus {
    PENDING,
    APPROVED,
    REJECTED
}
