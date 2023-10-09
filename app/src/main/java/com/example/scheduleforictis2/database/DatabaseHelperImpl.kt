package com.example.scheduleforictis2.database

import com.example.scheduleforictis2.ui.models.WeekSchedule
import com.example.scheduleforictis2.utils.ParserModels
import kotlinx.coroutines.flow.Flow

class DatabaseHelperImpl(private val database: Database): DatabaseHelper {
    override suspend fun insert(weekSchedule: WeekSchedule) =
        database.weekScheduleDao().insert(weekSchedule)


    override suspend fun delete(weekSchedule: WeekSchedule) =
        database.weekScheduleDao().delete(weekSchedule)


    override fun getWeekScheduleByGroupAndWeekNum(groupID: String, weekNum: Int): Flow<WeekSchedule> =
        database.weekScheduleDao().getWeekScheduleByGroupAndWeekNum(ParserModels.getID(weekNum, groupID))
}