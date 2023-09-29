package com.example.scheduleforictis2.utils.ext

import android.widget.SearchView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SearchViewExt {
    fun SearchView.getQueryTextChangeStateFlow(): StateFlow<String> {

        val query = MutableStateFlow("")

        setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                query.value = newText
                return true
            }
        })

        return query

    }
}