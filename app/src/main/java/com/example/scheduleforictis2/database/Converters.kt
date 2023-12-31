package com.example.scheduleforictis2.database

import androidx.room.TypeConverter
import com.example.scheduleforictis2.ui.models.Couple
import com.example.scheduleforictis2.ui.models.DaySchedule
import com.example.scheduleforictis2.ui.schedule.ScheduleItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun stringToDayEnt(dayEnt: String?): List<DaySchedule> {
        val gson = Gson()
        if (dayEnt == null) {
            emptyList<Any>()
        }
        val listType = object : TypeToken<List<DaySchedule?>?>() {}.type
        return gson.fromJson(dayEnt, listType)
    }

    @TypeConverter
    fun dayEntToString(list: List<DaySchedule?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun stringToCoupleEnt(coupleEnt: String?): List<ScheduleItem> {
        val gson = Gson()
        if (coupleEnt == null) {
            emptyList<Any>()
        }
        val listType = object : TypeToken<List<ScheduleItem?>?>() {}.type
        return gson.fromJson(coupleEnt, listType)
    }

    @TypeConverter
    fun coupleEntToString(list: List<ScheduleItem?>?): String {
        val gson = Gson()

        return gson.toJson(list)
    }
}