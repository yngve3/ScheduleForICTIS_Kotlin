package com.example.scheduleforictis2.ui.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "week_schedule")
data class WeekSchedule(
    @PrimaryKey val id: Int,
    val weekNum: Int,
    val groupID: String,
    val days: List<DaySchedule>,
    val countWeek: Int
) {
    fun getDay(position: Int): DaySchedule {
        return days[position]
    }
}