package com.example.scheduleforictis2.network

import com.example.scheduleforictis2.network.models.RequestModel
import retrofit2.Response

interface ApiHelper {
    suspend fun getGroupScheduleByID(groupID: String?): Response<RequestModel>

    suspend fun getGroupScheduleByIDAndWeek(
        groupID: String?,
        week: Int
    ): Response<RequestModel>

    suspend fun searchGroupByName(nameOfGroup: String?): Response<RequestModel>
}