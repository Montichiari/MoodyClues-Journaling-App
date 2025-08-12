package com.example.nus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nus.api.ApiClient
import com.example.nus.model.JournalLinkRequestDto
import com.example.nus.model.LinkRequest
import com.example.nus.model.LinkRequestStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InviteNotificationViewModel : ViewModel() {
    
    // 邀请请求列表
    private val _inviteRequests = MutableStateFlow<List<LinkRequest>>(emptyList())
    val inviteRequests: StateFlow<List<LinkRequest>> = _inviteRequests.asStateFlow()

    // 邀请数量
    private val _inviteCount = MutableStateFlow(0)
    val inviteCount: StateFlow<Int> = _inviteCount.asStateFlow()
    
    // 加载状态
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // 错误消息
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    // 成功消息
    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()
    
    // 当前用户ID
    private var journalUserId: String = ""
    
    fun setJournalUserId(id: String) {
        journalUserId = id
        loadInviteRequests()
    }
    
    fun loadInviteRequests() {
        if (journalUserId.isEmpty()) return
        
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                println("InviteNotificationViewModel: Loading invite requests for user: $journalUserId")
                
                val response = ApiClient.linkRequestApiService.getJournalUserLinkRequests(journalUserId)
                
                if (response.isSuccessful) {
                    val requests = response.body() ?: emptyList()
                    // 只显示待处理的邀请
                    val pendingRequests = requests.filter { it.status == LinkRequestStatus.PENDING }
                    _inviteRequests.value = pendingRequests
                    _inviteCount.value = pendingRequests.size

                    println("InviteNotificationViewModel: Loaded ${pendingRequests.size} pending invite requests")
                } else {
                    val errorMessage = "Failed to load invitations: ${response.message()}"
                    println("InviteNotificationViewModel: API error: ${response.code()} - ${response.message()}")
                    _errorMessage.value = errorMessage
                }
            } catch (e: Exception) {
                val errorMessage = "Network error: ${e.message}"
                println("InviteNotificationViewModel: Exception: $errorMessage")
                _errorMessage.value = errorMessage
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun acceptInvite(requestId: String) {
        handleInviteDecision(requestId, true)
    }
    
    fun declineInvite(requestId: String) {
        handleInviteDecision(requestId, false)
    }
    
    private fun handleInviteDecision(requestId: String, approved: Boolean) {
        if (journalUserId.isEmpty()) {
            _errorMessage.value = "User ID not set"
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _successMessage.value = null
            
            try {
                println("InviteNotificationViewModel: ${if (approved) "Accepting" else "Declining"} invite request: $requestId")
                
                val decision = JournalLinkRequestDto(approved = approved)
                val response = ApiClient.linkRequestApiService.decideLinkRequest(
                    requestId = requestId,
                    journalUserId = journalUserId,
                    decision = decision
                )
                
                if (response.isSuccessful) {
                    val action = if (approved) "accepted" else "declined"
                    _successMessage.value = "Invitation $action successfully"
                    
                    // 重新加载邀请列表
                    loadInviteRequests()
                    
                    println("InviteNotificationViewModel: Invite $action successfully")
                } else {
                    val errorMessage = "Failed to ${if (approved) "accept" else "decline"} invitation: ${response.message()}"
                    println("InviteNotificationViewModel: API error: ${response.code()} - ${response.message()}")
                    _errorMessage.value = errorMessage
                }
            } catch (e: Exception) {
                val errorMessage = "Network error: ${e.message}"
                println("InviteNotificationViewModel: Exception: $errorMessage")
                _errorMessage.value = errorMessage
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }
    
    fun refreshInvites() {
        loadInviteRequests()
    }
}
