package com.example.scheduleforictis2.utils

import com.example.scheduleforictis2.application.App
import com.example.scheduleforictis2.ui.models.Group

object User {
    val isAuthorized: Boolean
    var vpk: Group? = App.instance!!.load(true)
        set(value) {
            App.instance!!.save(value, true)
            field = value
        }

    var group: Group? = App.instance!!.load(false)
        set(value) {
            this.vpk = null
            App.instance!!.save(value, false)
            field = value
        }

    init {
        isAuthorized = group != null
    }
}