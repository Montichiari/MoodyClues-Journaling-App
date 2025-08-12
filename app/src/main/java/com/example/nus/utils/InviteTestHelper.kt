package com.example.nus.utils

import com.example.nus.api.ApiClient
import com.example.nus.model.CounsellorLinkRequestDto
import com.example.nus.model.JournalLinkRequestDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object InviteTestHelper {
    
    fun testCreateInvite(
        counsellorId: String,
        clientEmail: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                println("Testing invite creation for counsellor: $counsellorId, client: $clientEmail")
                
                val request = CounsellorLinkRequestDto(clientEmail = clientEmail)
                val response = ApiClient.linkRequestApiService.createLinkRequest(counsellorId, request)
                
                if (response.isSuccessful) {
                    val linkRequest = response.body()
                    val successMsg = "Invite created successfully. Request ID: ${linkRequest?.id}"
                    println(successMsg)
                    onSuccess(successMsg)
                } else {
                    val errorMsg = "Failed to create invite: ${response.code()} - ${response.message()}"
                    println(errorMsg)
                    onError(errorMsg)
                }
            } catch (e: Exception) {
                val errorMsg = "Exception creating invite: ${e.message}"
                println(errorMsg)
                onError(errorMsg)
            }
        }
    }
    
    fun testGetInvites(
        journalUserId: String,
        onSuccess: (Int) -> Unit,
        onError: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                println("Testing get invites for user: $journalUserId")
                
                val response = ApiClient.linkRequestApiService.getJournalUserLinkRequests(journalUserId)
                
                if (response.isSuccessful) {
                    val requests = response.body() ?: emptyList()
                    val pendingCount = requests.count { it.status.name == "PENDING" }
                    val successMsg = "Found ${requests.size} total invites, $pendingCount pending"
                    println(successMsg)
                    onSuccess(pendingCount)
                } else {
                    val errorMsg = "Failed to get invites: ${response.code()} - ${response.message()}"
                    println(errorMsg)
                    onError(errorMsg)
                }
            } catch (e: Exception) {
                val errorMsg = "Exception getting invites: ${e.message}"
                println(errorMsg)
                onError(errorMsg)
            }
        }
    }
    
    fun testDecideInvite(
        requestId: String,
        journalUserId: String,
        approved: Boolean,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                println("Testing invite decision: requestId=$requestId, userId=$journalUserId, approved=$approved")
                
                val decision = JournalLinkRequestDto(approved = approved)
                val response = ApiClient.linkRequestApiService.decideLinkRequest(requestId, journalUserId, decision)
                
                if (response.isSuccessful) {
                    val action = if (approved) "accepted" else "declined"
                    val successMsg = "Invite $action successfully"
                    println(successMsg)
                    onSuccess(successMsg)
                } else {
                    val errorMsg = "Failed to decide invite: ${response.code()} - ${response.message()}"
                    println(errorMsg)
                    onError(errorMsg)
                }
            } catch (e: Exception) {
                val errorMsg = "Exception deciding invite: ${e.message}"
                println(errorMsg)
                onError(errorMsg)
            }
        }
    }
}
