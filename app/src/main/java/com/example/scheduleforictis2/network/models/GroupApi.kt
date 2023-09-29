package com.example.scheduleforictis2.network.models


import com.google.gson.annotations.SerializedName

data class GroupApi(
    @SerializedName("group")
    val group: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    var isSelected: Boolean = false
)