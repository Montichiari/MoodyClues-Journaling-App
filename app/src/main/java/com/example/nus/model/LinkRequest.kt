package com.example.nus.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class LinkRequest(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("counsellorUser")
    val counsellorUser: CounsellorUser?,
    
    @SerializedName("journalUser")
    val journalUser: JournalUser?,
    
    @SerializedName("requestedAt")
    val requestedAt: String, // 使用String来接收ISO格式的日期时间
    
    @SerializedName("status")
    val status: LinkRequestStatus
)

enum class LinkRequestStatus {
    @SerializedName("PENDING")
    PENDING,
    
    @SerializedName("ACCEPTED")
    ACCEPTED,
    
    @SerializedName("DECLINED")
    DECLINED
}

data class CounsellorUser(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("firstName")
    val firstName: String?,
    
    @SerializedName("lastName")
    val lastName: String?
)

data class JournalUser(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("firstName")
    val firstName: String?,
    
    @SerializedName("lastName")
    val lastName: String?
)

// 创建邀请请求的DTO
data class CounsellorLinkRequestDto(
    @SerializedName("clientEmail")
    val clientEmail: String
)

// 处理邀请决定的DTO
data class JournalLinkRequestDto(
    @SerializedName("approved")
    val approved: Boolean
)

// API响应包装类
data class LinkRequestResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String?,
    
    @SerializedName("data")
    val data: LinkRequest?
)
