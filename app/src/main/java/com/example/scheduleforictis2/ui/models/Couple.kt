package com.example.scheduleforictis2.ui.models

import com.example.scheduleforictis2.utils.ParserModels.hash

data class Couple(
    val numOfCouple: Int,
    val discipline: String,
    val audience: String,
    val professor: String,
    val kind: KindOfCouple,
    val isOnline: Boolean
) {

    enum class KindOfCouple {
        PRACTICE,
        LABORATORY,
        LECTURE,
        EXAM,
        UNDEFINED
    }

    fun getTimeStart(): String {
        return when (numOfCouple) {
            1 -> "08:00"
            2 -> "09:50"
            3 -> "11:55"
            4 -> "13:45"
            5 -> "15:50"
            6 -> "17:40"
            7 -> "19:30"
            else -> "nan"
        }
    }

    fun getTimeEnd(): String {
        return when (numOfCouple) {
            1 -> "09:35"
            2 -> "11:25"
            3 -> "13:30"
            4 -> "15:20"
            5 -> "17:25"
            6 -> "19:15"
            7 -> "21:05"
            else -> "nan"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Couple

        if (numOfCouple != other.numOfCouple) return false
        if (discipline != other.discipline) return false
        if (audience != other.audience) return false
        if (professor != other.professor) return false
//        if (kind != other.kind) return false
        if (isOnline != other.isOnline) return false

        return true
    }

    override fun hashCode(): Int {
        val isOnlineInt = if (isOnline) 12
        else 22
        return isOnlineInt + discipline.hash() + audience.hash()
    }
}