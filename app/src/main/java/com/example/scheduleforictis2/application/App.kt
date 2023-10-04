package com.example.scheduleforictis2.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.scheduleforictis2.ConnectionHelper
import com.example.scheduleforictis2.database.DatabaseBuilder
import com.example.scheduleforictis2.database.DatabaseHelperImpl
import com.example.scheduleforictis2.ui.models.Group

class App: Application() {

    var databaseHelper: DatabaseHelperImpl? = null
    private val nameSharedPref = "vpkStore"
    private val nameVpkVar = "vpkName"
    private val idVpkVar = "vpkId"

    private val groupNameVar = "groupName"
    private val groupIdVar = "groupId"

    override fun onCreate() {
        super.onCreate()
        instance = this
        databaseHelper = DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext))
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        ConnectionHelper.init(applicationContext)
    }

    fun load(isVPK: Boolean): Group? {
        val nameVar = if (isVPK) nameVpkVar else groupNameVar
        val idVar = if (isVPK) idVpkVar else groupIdVar

        val id = getSharedPreferences(nameSharedPref, MODE_PRIVATE)
            .getString(idVar, null)
        val name = getSharedPreferences(nameSharedPref, MODE_PRIVATE)
            .getString(nameVar, null)

        return if (id != null && name != null) Group(name, id) else null
    }

    fun save(group: Group?, isVPK: Boolean) {

        val name = if (isVPK) nameVpkVar else groupNameVar
        val id = if (isVPK) idVpkVar else groupIdVar

        getSharedPreferences(nameSharedPref, MODE_PRIVATE).edit()
            .putString(name, group?.name)
            .putString(id, group?.id)
            .apply()
    }


    companion object {
        var instance: App? = null
    }

}