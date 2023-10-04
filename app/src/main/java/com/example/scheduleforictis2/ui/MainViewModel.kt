package com.example.scheduleforictis2.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainViewModel: ViewModel() {

    private val TAG = "MainViewModel.TAG"

    val weekScheduleLiveData: MutableLiveData<WeekSchedule> = MutableLiveData()
    private lateinit var selectedWeekSchedule: WeekSchedule

    fun saveGroup(group: Group?, isVPK: Boolean = false) {
        if (isVPK) {
            User.vpk = group
        } else {
            User.group = group
        }
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
                if (User.vpk != null) {
                    ScheduleRepository.getGroupScheduleByIDAndWeek(User.vpk!!.id, weekNum).collect {
                        selectedWeekSchedule.setVPK(it)
                    }
                }

                weekScheduleLiveData.postValue(selectedWeekSchedule)
            } catch (e: Exception) {
                Log.e(TAG, e.message?: "")
            }
        }
    }

    //TODO Добавить состояния запросов
    private val query = MutableStateFlow("")

    var queryText: String
        get() = query.value
        set(value) { query.value = value }

    @OptIn(FlowPreview::class)
    fun filteredCategories() = query
        .debounce(400)
        .distinctUntilChanged()
        .map { search(it.lowercase().trim()) }
        .asLiveData()

    suspend fun search(request: String): List<GroupApi> {
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
}