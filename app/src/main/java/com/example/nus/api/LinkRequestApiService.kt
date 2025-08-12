package com.example.nus.api

import com.example.nus.model.CounsellorLinkRequestDto
import com.example.nus.model.JournalLinkRequestDto
import com.example.nus.model.LinkRequest
import retrofit2.Response
import retrofit2.http.*

interface LinkRequestApiService {
    
    // 创建邀请请求 (Counsellor发送邀请给Journal User)
    @POST("api/linkrequest/{counsellorId}")
    suspend fun createLinkRequest(
        @Path("counsellorId") counsellorId: String,
        @Body request: CounsellorLinkRequestDto
    ): Response<LinkRequest>
    
    // 获取Counsellor发出的所有邀请请求
    @GET("api/linkrequest/counsellor/all-link-requests/{counsellorId}")
    suspend fun getCounsellorLinkRequests(
        @Path("counsellorId") counsellorId: String
    ): Response<List<LinkRequest>>
    
    // 获取Journal User收到的所有邀请请求
    @GET("api/linkrequest/journal/all-link-requests/{journalUserId}")
    suspend fun getJournalUserLinkRequests(
        @Path("journalUserId") journalUserId: String
    ): Response<List<LinkRequest>>
    
    // Journal User处理邀请请求 (接受或拒绝)
    @POST("api/linkrequest/{requestId}/decision/{journalUserId}")
    suspend fun decideLinkRequest(
        @Path("requestId") requestId: String,
        @Path("journalUserId") journalUserId: String,
        @Body decision: JournalLinkRequestDto
    ): Response<Void>
}
