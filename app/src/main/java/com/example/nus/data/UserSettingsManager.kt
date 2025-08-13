package com.example.nus.data

import android.content.Context
import android.content.SharedPreferences

class UserSettingsManager(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_settings", Context.MODE_PRIVATE)
    
    companion object {
        @Volatile
        private var INSTANCE: UserSettingsManager? = null
        
        private const val KEY_SHOW_EMOTION = "show_emotion"
        
        fun getInstance(context: Context): UserSettingsManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserSettingsManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    
    /**
     * 获取是否显示情绪设置
     */
    fun getShowEmotion(): Boolean {
        return prefs.getBoolean(KEY_SHOW_EMOTION, true) // 默认显示情绪
    }
    
    /**
     * 设置是否显示情绪
     */
    fun setShowEmotion(showEmotion: Boolean) {
        prefs.edit().putBoolean(KEY_SHOW_EMOTION, showEmotion).apply()
        println("UserSettingsManager: Show emotion set to $showEmotion")
    }
    
    /**
     * 切换情绪显示状态
     */
    fun toggleShowEmotion(): Boolean {
        val newValue = !getShowEmotion()
        setShowEmotion(newValue)
        return newValue
    }
    
    /**
     * 清除所有设置
     */
    fun clearAllSettings() {
        prefs.edit().clear().apply()
        println("UserSettingsManager: All settings cleared")
    }
}
