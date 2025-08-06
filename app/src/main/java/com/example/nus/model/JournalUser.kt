package com.example.nus.model


data class JournalUser(
    val showEmotion: Boolean,
    val journalEntries: List<JournalEntry>,
    val counsellors: List<CounsellorUser>,
    val linkRequests: List<LinkRequest>,
    val user : User
)
