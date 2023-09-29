package com.example.scheduleforictis2.network.models


import com.google.gson.annotations.SerializedName

data class RequestModel(
    @SerializedName("table")
    val table: Table,
    @SerializedName("weeks")
    val weeks: List<Int>,
    @SerializedName("choices")
    val groups: List<GroupApi>
)