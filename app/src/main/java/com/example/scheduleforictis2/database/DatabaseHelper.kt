package com.example.scheduleforictis2.database

import com.example.scheduleforictis2.ui.models.WeekSchedule
import kotlinx.coroutines.flow.Flow

interface DatabaseHelper {
    suspend fun insert(weekSchedule: WeekSchedule)

    suspend fun delete(weekSchedule: WeekSchedule)

    fun getWeekScheduleByGroupAndWeekNum(groupID: String, weekNum: Int): Flow<WeekSchedule>
}