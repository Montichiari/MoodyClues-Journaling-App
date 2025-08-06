package com.example.nus.model

data class CounsellorUser (
    val clients: List<JournalUser>,
    val linkRequests: List<LinkRequest>
)