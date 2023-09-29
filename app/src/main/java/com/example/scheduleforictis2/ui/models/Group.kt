package com.example.scheduleforictis2.ui.models

import com.example.scheduleforictis2.utils.ParserModels.hash

data class Group(
    val name: String,
    val id: String
) {
    override fun hashCode(): Int {
        return name.hash() * id.hash()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Group

        if (name != other.name) return false
        if (id != other.id) return false

        return true
    }
}