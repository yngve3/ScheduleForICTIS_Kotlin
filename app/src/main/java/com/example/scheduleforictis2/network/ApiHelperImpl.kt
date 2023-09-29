package com.example.scheduleforictis2.network

import com.example.scheduleforictis2.network.models.RequestModel
import retrofit2.Response

class ApiHelperImpl(private val api: Api): ApiHelper {
    override suspend fun getGroupScheduleByID(groupID: String?): Response<RequestModel> {
        return api.getGroupScheduleByID(groupID)
    }

    override suspend fun getGroupScheduleByIDAndWeek(groupID: String?, week: Int): Response<RequestModel> {
        return api.getGroupScheduleByIDAndWeek(groupID, week)
    }

    override suspend fun searchGroupByName(nameOfGroup: String?): Response<RequestModel> {
        return api.searchGroupByName(nameOfGroup)
    }
}