package com.example.scheduleforictis2.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.scheduleforictis2.application.App
import com.example.scheduleforictis2.network.models.GroupApi
import com.example.scheduleforictis2.repositories.ScheduleRepository
import com.example.scheduleforictis2.ui.models.Group
import com.example.scheduleforictis2.ui.models.WeekSchedule
import com.example.scheduleforictis2.utils.ParserModels.setVPK
import com.example.scheduleforictis2.utils.User
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainViewModel: ViewModel() {

    private val TAG = "MainViewModel.TAG"

    val weekScheduleLiveData: MutableLiveData<WeekSchedule> = MutableLiveData()
    private lateinit var selectedWeekSchedule: WeekSchedule
    private var vpk: Group?

    fun saveGroup(group: Group?, isVPK: Boolean = false) {
        if (isVPK) {
            this.vpk = group
            User.vpk = group
        } else {
            this.vpk = null
            User.group = group
        }
    }

    init {
        vpk = App.instance!!.load(true)
    }

    fun changeWeekSchedule(groupID: String, offset: Int) {
        getGroupScheduleByIDAndWeek(groupID, offset)
    }

    private fun getGroupScheduleByIDAndWeek(groupID: String, weekNum: Int) {
        viewModelScope.launch {
            try {

                ScheduleRepository.getGroupScheduleByIDAndWeek(groupID, weekNum).collect {
                    selectedWeekSchedule = it
                }

                //TODO Переделать
                if (vpk != null) {
                    ScheduleRepository.getGroupScheduleByIDAndWeek(vpk!!.id, weekNum).collect {
                        selectedWeekSchedule.setVPK(it)
                    }
                }

                weekScheduleLiveData.postValue(selectedWeekSchedule)
            } catch (e: Exception) {
                Log.e(TAG, e.message?: "")
            }
        }
    }

    private val query = MutableStateFlow("")

    var queryText: String
        get() = query.value
        set(value) { query.value = value }

    @OptIn(FlowPreview::class)
    val filteredCategories = query
        .debounce(500) // maybe bigger to avoid too many queries
        .distinctUntilChanged()
        .map {
            val criteria = it.lowercase()
            test(criteria) // up to you to implement this depending on source
        }
        .asLiveData()

    suspend fun test(request: String): List<GroupApi> {
        try {
            val response = ScheduleRepository.search(request)
            with (response.body()!!) {
                if (groups != null) {
                    return groups
                } else if (table != null) {
                    return listOf(GroupApi(table.group, "id", table.name))
                } else
                    return groups
            }
        } catch (e: HttpException) {
            Log.e(TAG, e.message())
        } catch (e: IOException) {
            Log.e(TAG, e.message?: "Unknown error")
        } catch (e: Exception) {
            Log.e(TAG, e.message?: "Unknown error")
        }

        return emptyList()
    }


    fun search(request: String): LiveData<List<GroupApi>> {
        val result = MutableLiveData<List<GroupApi>>()
        viewModelScope.launch {
            try {
                val response = ScheduleRepository.search(request)
                with (response.body()!!) {
                    if (groups != null) {
                        result.postValue(groups)
                    } else if (table != null) {
                        result.postValue(listOf(GroupApi(table.group, "id", table.name)))
                    } else result.postValue(groups)
                }
            } catch (e: HttpException) {
                Log.e(TAG, e.message())
            } catch (e: IOException) {
                Log.e(TAG, e.message?: "Unknown error")
            } catch (e: Exception) {
                Log.e(TAG, e.message?: "Unknown error")
            }
        }

        return result
    }
}