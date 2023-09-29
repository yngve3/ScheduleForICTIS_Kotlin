package com.example.scheduleforictis2.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.scheduleforictis2.ui.models.WeekSchedule

@Database(entities = [WeekSchedule::class], version = 1)
@TypeConverters(Converters::class)
abstract class Database: RoomDatabase() {
    abstract fun weekScheduleDao(): WeekScheduleDao
}