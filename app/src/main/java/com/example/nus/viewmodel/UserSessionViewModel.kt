package com.example.nus.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.example.nus.data.UserSettingsManager

class UserSessionViewModel(application: Application) : AndroidViewModel(application) {
    private val userSettingsManager = UserSettingsManager.getInstance(application)

    val userId = mutableStateOf("")
    val isLoggedIn = mutableStateOf(false)
    val showEmotion = mutableStateOf(userSettingsManager.getShowEmotion())

    // 保存用户认证信息用于API调用
    val userEmail = mutableStateOf("")
    val userPassword = mutableStateOf("")

    fun setUserSession(userId: String, showEmotion: Boolean, email: String, password: String) {
        this.userId.value = userId
        this.showEmotion.value = showEmotion
        this.userEmail.value = email
        this.userPassword.value = password
        this.isLoggedIn.value = true

        // 保存到本地设置
        userSettingsManager.setShowEmotion(showEmotion)
    }

    fun clearSession() {
        userId.value = ""
        showEmotion.value = false
        userEmail.value = ""
        userPassword.value = ""
        isLoggedIn.value = false
    }

    /**
     * 切换情绪显示设置
     */
    fun toggleShowEmotion() {
        val newValue = userSettingsManager.toggleShowEmotion()
        showEmotion.value = newValue
        println("UserSessionViewModel: Show emotion toggled to $newValue")
    }

    /**
     * 设置情绪显示
     */
    fun setShowEmotion(show: Boolean) {
        userSettingsManager.setShowEmotion(show)
        showEmotion.value = show
        println("UserSessionViewModel: Show emotion set to $show")
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
