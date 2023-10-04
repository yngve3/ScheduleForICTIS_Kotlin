package com.example.scheduleforictis2.utils

import com.example.scheduleforictis2.network.models.GroupApi
import com.example.scheduleforictis2.network.models.RequestModel
import com.example.scheduleforictis2.ui.models.Couple
import com.example.scheduleforictis2.ui.models.DaySchedule
import com.example.scheduleforictis2.ui.models.Group
import com.example.scheduleforictis2.ui.models.WeekSchedule

object ParserModels {

    fun String.hash(): Int {
        var res = 0
        this.forEach { res += it.code }
        return res
    }

    fun GroupApi.asGroup(): Group = Group(this.name, this.group)

    fun RequestModel.asWeekSchedule(): WeekSchedule {
        val weekNum = this.table.week
        val groupID = this.table.group

        val week = this.table.table

        val daySchedules = ArrayList<DaySchedule>()

        for (i in 2 until week.size) {
            val day = week[i]
            val date = day[0]

            val weekdayNum = DateHelper.getWeekdayNum(date.split(",")[0])
            val dayOfMonth = date.split(",")[1].split(" ")[0].toInt()
            val month = DateHelper.getMonthNum(date.split(",")[1].split(" ")[1])

            val couples = mutableListOf<Couple>()
            var isWAR = false
            var isVPK = false
            for (coupleNum in 1..7) {
                val couple = getCoupleFromStr(day[coupleNum], coupleNum)
                if (couple.discipline.isNotEmpty()) {
                    if (couple.discipline.contains("ВПК")) {
                        isVPK = true
                        break
                    } else if (couple.discipline.contains("ВУЦ")) {
                        isWAR = true
                    } else {
                        couples.add(couple)
                    }
                }
            }

            daySchedules.add(DaySchedule(weekdayNum, dayOfMonth, month, weekNum, couples, couples.isEmpty(), isWAR, isVPK))
        }

        return WeekSchedule(getID(weekNum, groupID), weekNum, groupID, daySchedules, this.weeks.size)
    }

    fun WeekSchedule.setVPK(vpkSchedule: WeekSchedule) {
        for (day in this.days) {
            if (day.isVPK and !day.vpkIsChosen) {
                day.couples.addAll(vpkSchedule.getDay(day.weekdayNum - 1).couples)
                day.vpkIsChosen = true
            }
        }
    }

    fun getID(weekNum: Int, groupID: String) = (weekNum.toString() + groupID.removeSuffix(".html").removeRange(0, 1)).toInt()


    private fun getCoupleFromStr(str: String, numOfCouple: Int): Couple {
        var temp = str

        val kind = "пр.|лаб.|лек.|экз.".toRegex().find(temp)?.value ?: ""
        temp = temp.removePrefix(kind)

        val isOnline = temp.contains("LMS")
        //TODO Придумать что делать, если у пары несколько преподавателей в разных аудиториях

        val audiences = "LMS(-[0-9]| |$)|[А-Я]-[0-9]{3}[а-я]?".toRegex()
            .findAll(temp)
            .map { it.value }
            .onEach { temp = temp.replace(it, "") }
            .map { it.removeSuffix("-1") }
            .joinToString("\n")

        val professors = "([1-9] п/г)? [А-Я][а-я]* [А-Я]. [A-Я].".toRegex()
            .findAll(temp)
            .map { it.value.trim() }
            .onEach { temp = temp.replace(it, "") }
            .joinToString("\n")


        return Couple(numOfCouple, temp.trim(), audiences, professors, getKind(kind), isOnline)
    }

    private fun getKind(kind: String): Couple.KindOfCouple {
        return when(kind) {
            "пр." -> Couple.KindOfCouple.PRACTICE
            "лаб." -> Couple.KindOfCouple.LABORATORY
            "лек." -> Couple.KindOfCouple.LECTURE
            "экз." -> Couple.KindOfCouple.EXAM
            else -> Couple.KindOfCouple.UNDEFINED
        }
    }

}