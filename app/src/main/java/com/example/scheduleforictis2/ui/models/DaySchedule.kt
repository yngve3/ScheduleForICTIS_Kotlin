package com.example.scheduleforictis2.ui.models

import com.example.scheduleforictis2.ui.schedule.ScheduleItem

data class DaySchedule(
    val weekdayNum: Int,
    val dayOfMonth: Int,
    val month: Int,
    val weekNum: Int,
    val couples: MutableList<ScheduleItem>
)