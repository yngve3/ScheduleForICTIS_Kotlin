package com.example.scheduleforictis2.ui.models

data class DaySchedule(
    val weekdayNum: Int,
    val dayOfMonth: Int,
    val month: Int,
    val weekNum: Int,
    val couples: MutableList<Couple>,
    val isBlank: Boolean = false,
    val isWar: Boolean = false,
    val isVPK: Boolean = false,
    var vpkIsChosen: Boolean = false
)