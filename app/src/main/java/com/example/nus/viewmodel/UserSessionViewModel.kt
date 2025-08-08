package com.example.nus.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class UserSessionViewModel : ViewModel() {
    val userId = mutableStateOf("")
    val isLoggedIn = mutableStateOf(false)
    val showEmotion = mutableStateOf(false)

    // 保存用户认证信息用于API调用
    val userEmail = mutableStateOf("")
    val userPassword = mutableStateOf("")

    fun setUserSession(userId: String, showEmotion: Boolean, email: String, password: String) {
        this.userId.value = userId
        this.showEmotion.value = showEmotion
        this.userEmail.value = email
        this.userPassword.value = password
        this.isLoggedIn.value = true
    }

    fun clearSession() {
        userId.value = ""
        showEmotion.value = false
        userEmail.value = ""
        userPassword.value = ""
        isLoggedIn.value = false
    }

    // 获取Basic Auth凭据
    fun getBasicAuthCredentials(): String? {
        return if (userEmail.value.isNotEmpty() && userPassword.value.isNotEmpty()) {
            val credentials = "${userEmail.value}:${userPassword.value}"
            android.util.Base64.encodeToString(credentials.toByteArray(), android.util.Base64.NO_WRAP)
        } else {
            null
        }
    }
}
