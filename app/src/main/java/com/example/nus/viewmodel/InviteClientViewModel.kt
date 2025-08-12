package com.example.nus.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nus.api.ApiClient
import com.example.nus.model.CounsellorLinkRequestDto
import com.example.nus.model.LinkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InviteClientViewModel : ViewModel() {
    
    // UI状态
    private val _clientEmail = MutableStateFlow("")
    val clientEmail: StateFlow<String> = _clientEmail.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()
    
    private val _isInviteSent = MutableStateFlow(false)
    val isInviteSent: StateFlow<Boolean> = _isInviteSent.asStateFlow()
    
    // 当前Counsellor ID
    private var counsellorId: String = ""
    
    fun setCounsellorId(id: String) {
        counsellorId = id
    }
    
    fun updateClientEmail(email: String) {
        _clientEmail.value = email
        // 清除之前的错误和成功消息
        _errorMessage.value = null
        _successMessage.value = null
        _isInviteSent.value = false
    }
    
    fun sendInvite() {
        if (counsellorId.isEmpty()) {
            _errorMessage.value = "Counsellor ID not set"
            return
        }
        
        val email = _clientEmail.value.trim()
        if (email.isEmpty()) {
            _errorMessage.value = "Please enter a valid email address"
            return
        }
        
        if (!isValidEmail(email)) {
            _errorMessage.value = "Please enter a valid email format"
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _successMessage.value = null
            
            try {
                println("InviteClientViewModel: Sending invite to $email from counsellor $counsellorId")
                
                val request = CounsellorLinkRequestDto(clientEmail = email)
                val response = ApiClient.linkRequestApiService.createLinkRequest(counsellorId, request)
                
                if (response.isSuccessful) {
                    val linkRequest = response.body()
                    println("InviteClientViewModel: Invite sent successfully. Request ID: ${linkRequest?.id}")
                    _successMessage.value = "Invitation sent successfully to $email"
                    _isInviteSent.value = true
                    _clientEmail.value = "" // 清空输入框
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = when (response.code()) {
                        400 -> "Invalid request. The client may already be linked or have a pending request."
                        404 -> "Client with email $email not found. Please check the email address."
                        409 -> "A pending invitation already exists for this client."
                        else -> "Failed to send invitation: ${response.message()}"
                    }
                    println("InviteClientViewModel: API error: ${response.code()} - $errorBody")
                    _errorMessage.value = errorMessage
                }
            } catch (e: Exception) {
                val errorMessage = "Network error: ${e.message}"
                println("InviteClientViewModel: Exception: $errorMessage")
                _errorMessage.value = errorMessage
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
        _isInviteSent.value = false
    }
    
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
