package com.example.scheduleforictis2.repositories

import com.example.scheduleforictis2.ConnectionHelper
import com.example.scheduleforictis2.application.App
import com.example.scheduleforictis2.network.ApiHelperImpl
import com.example.scheduleforictis2.network.NetworkService
import com.example.scheduleforictis2.ui.models.WeekSchedule
import com.example.scheduleforictis2.utils.ParserModels.asWeekSchedule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

object ScheduleRepository {

    private const val TAG = "ScheduleRepository.TAG"

    private val apiHelperImpl = ApiHelperImpl(NetworkService.apiService)

    suspend fun getGroupScheduleByIDAndWeek(groupID: String, weekNum: Int): Flow<WeekSchedule> =
        flow {
            emit(App.instance!!.databaseHelper!!.getWeekScheduleByGroupAndWeekNum(
                groupID,
                weekNum
            ).first())

            if (ConnectionHelper.isConnected) {
                val response = apiHelperImpl.getGroupScheduleByIDAndWeek(groupID, weekNum).body()!!.asWeekSchedule()
                App.instance!!.databaseHelper!!.insert(response)
            }
        }

    suspend fun search(request: String) = apiHelperImpl.searchGroupByName(request)
}