package com.example.scheduleforictis2.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.scheduleforictis2.ui.models.WeekSchedule
import kotlinx.coroutines.flow.Flow

@Dao
interface WeekScheduleDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(weekSchedule: WeekSchedule)

    @Delete
    suspend fun delete(weekSchedule: WeekSchedule)

    @Query("SELECT * FROM week_schedule WHERE `groupID`=:groupID AND `weekNum`=:weekNum")
    fun getWeekScheduleByGroupAndWeekNum(groupID: String, weekNum: Int): Flow<WeekSchedule>

    @Query("SELECT * FROM week_schedule WHERE `id`=:scheduleID")
    fun getWeekScheduleByGroupAndWeekNum(scheduleID: Int): Flow<WeekSchedule>

}