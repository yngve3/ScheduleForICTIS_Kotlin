package com.example.scheduleforictis2.network

import com.example.scheduleforictis2.network.models.RequestModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("schedule-api/")
    suspend fun getGroupScheduleByID(@Query("group") groupID: String?): Response<RequestModel>

    @GET("schedule-api/")
    suspend fun getGroupScheduleByIDAndWeek(
        @Query("group") groupID: String?,
        @Query("week") week: Int
    ): Response<RequestModel>

    @GET("schedule-api/")
    suspend fun searchGroupByName(@Query("query") nameOfGroup: String?): Response<RequestModel>
}