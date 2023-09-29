package com.example.scheduleforictis2.network.models


import com.google.gson.annotations.SerializedName

data class Table(
    @SerializedName("group")
    val group: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("table")
    val table: List<List<String>>,
    @SerializedName("type")
    val type: String,
    @SerializedName("week")
    val week: Int
)