package com.example.nus.api

import com.example.nus.model.JournalUserResponse
import com.example.nus.model.JournalEntryResponse
import com.example.nus.model.HabitsEntryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CounsellorClientApiService {
    
    /**
     * 获取咨询师的所有客户列表
     * 对应后端: GET /api/counsellor/clients/all
     */
    @GET("api/counsellor/clients/all")
    suspend fun listClients(): Response<List<JournalUserResponse>>
    
    /**
     * 获取指定客户的所有日志条目
     * 对应后端: GET /api/counsellor/clients/{journalUserId}/journal-entries
     */
    @GET("api/counsellor/clients/{journalUserId}/journal-entries")
    suspend fun listClientJournalEntries(@Path("journalUserId") journalUserId: String): Response<List<JournalEntryResponse>>

    /**
     * 获取指定客户的所有日志条目 (Android版本)
     * 对应后端: GET /api/counsellor/clients/{journalUserId}/journal-entries-android/{counsellorId}
     */
    @GET("api/counsellor/clients/{journalUserId}/journal-entries-android/{counsellorId}")
    suspend fun listClientJournalEntriesAndroid(
        @Path("journalUserId") journalUserId: String,
        @Path("counsellorId") counsellorId: String
    ): Response<List<JournalEntryResponse>>

    /**
     * 获取指定客户的所有习惯条目
     * 对应后端: GET /api/counsellor/clients/{journalUserId}/habits-entries
     */
    @GET("api/counsellor/clients/{journalUserId}/habits-entries")
    suspend fun listClientHabitsEntries(@Path("journalUserId") journalUserId: String): Response<List<HabitsEntryResponse>>

    /**
     * 获取指定客户的特定日志条目
     * 对应后端: GET /api/counsellor/clients/{journalUserId}/journal-entries/{entryId}
     */
    @GET("api/counsellor/clients/{journalUserId}/journal-entries/{entryId}")
    suspend fun getJournalEntry(
        @Path("journalUserId") journalUserId: String,
        @Path("entryId") entryId: String
    ): Response<JournalEntryResponse>

    /**
     * 获取指定客户的特定日志条目 (Android版本)
     * 对应后端: GET /api/counsellor/clients/{journalUserId}/journal-entries/{entryId}/{counsellorId}
     */
    @GET("api/counsellor/clients/{journalUserId}/journal-entries/{entryId}/{counsellorId}")
    suspend fun getJournalEntryAndroid(
        @Path("journalUserId") journalUserId: String,
        @Path("entryId") entryId: String,
        @Path("counsellorId") counsellorId: String
    ): Response<JournalEntryResponse>
    
    /**
     * 获取指定客户的特定习惯条目
     * 对应后端: GET /api/counsellor/clients/{journalUserId}/habits-entries/{entryId}
     */
    @GET("api/counsellor/clients/{journalUserId}/habits-entries/{entryId}")
    suspend fun getHabitsEntry(
        @Path("journalUserId") journalUserId: String,
        @Path("entryId") entryId: String
    ): Response<HabitsEntryResponse>
}
