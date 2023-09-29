package com.example.scheduleforictis2.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.scheduleforictis2.R
import com.example.scheduleforictis2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //TODO Добавление задач
    //TODO Заметки
    //TODO Держать в памяти больше фрагментов
    //TODO Отображать выбранные группы
    //TODO Свайпом в субботу переключать на следующую неделю

    //TODO По центру текст для Вовы
    //TODO Поиск в динамическом режиме
    //TODO Убрать отступы после toolbar
    //TODO Пофиксить если нет аудитории

    //TODO Виджет


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        binding.bottomNavBar.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.groupSelectionScreen -> binding.bottomNavBar.visibility = View.GONE
                else -> binding.bottomNavBar.visibility = View.VISIBLE
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            super.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}