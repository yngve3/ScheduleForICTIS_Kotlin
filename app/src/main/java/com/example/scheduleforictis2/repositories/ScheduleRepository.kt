package com.example.scheduleforictis2.repositories

import com.example.scheduleforictis2.application.App
import com.example.scheduleforictis2.network.ApiHelperImpl
import com.example.scheduleforictis2.network.NetworkService
import com.example.scheduleforictis2.ui.models.WeekSchedule
import com.example.scheduleforictis2.utils.ParserModels.asWeekSchedule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

object ScheduleRepository {

    private const val TAG = "ScheduleRepository.TAG"

    private val apiHelperImpl = ApiHelperImpl(NetworkService.apiService)

    suspend fun getGroupScheduleByIDAndWeek(groupID: String, weekNum: Int): Flow<WeekSchedule> =
        flow {
            val responseApi = apiHelperImpl.getGroupScheduleByIDAndWeek(groupID, weekNum).body()
            val response = responseApi!!.asWeekSchedule()
            App.instance!!.databaseHelper!!.insert(response)
            emit(response)
        }.catch {
            emit(App.instance!!.databaseHelper!!.getWeekScheduleByGroupAndWeekNum(
                groupID,
                weekNum
            ).first())
        }

    suspend fun search(request: String) = apiHelperImpl.searchGroupByName(request)
}