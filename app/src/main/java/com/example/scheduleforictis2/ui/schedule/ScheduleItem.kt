package com.example.scheduleforictis2.ui.schedule

import com.example.scheduleforictis2.ui.models.Couple

open class ScheduleItem(val type: ScheduleItemType) {
    enum class ScheduleItemType {
        COUPLE,
        TOVARISH,
        MOTHER,
        ADD_VPK
    }

    var couple: Couple? = null

    constructor(couple: Couple): this(ScheduleItemType.COUPLE) {
        this.couple = couple
    }

    override fun hashCode(): Int {
        return if (couple == null) type.ordinal
        else couple.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ScheduleItem

        if (type != other.type) return false

        return true
    }
}