package com.example.scheduleforictis2.ui.schedule

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.scheduleforictis2.databinding.FragmentScheduleBinding
import com.example.scheduleforictis2.ui.MainViewModel
import com.example.scheduleforictis2.ui.schedule.one_row_calendar.Date
import com.example.scheduleforictis2.ui.schedule.one_row_calendar.DateHelper
import com.example.scheduleforictis2.ui.schedule.one_row_calendar.OneRowCalendar
import com.example.scheduleforictis2.ui.schedule.one_row_calendar.ViewPagerDaysOfWeekAdapter
import com.example.scheduleforictis2.utils.User
import java.time.LocalDate

class ScheduleFragment: Fragment() {

    private lateinit var binding: FragmentScheduleBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (viewModel.countWeeks == 0) viewModel.countWeeks = DateHelper.getCurrDate().weekNum


        with(binding) {
            calendar.addViewPagerAdapter(
                ViewPagerDaysOfWeekAdapter(
                    childFragmentManager,
                    lifecycle,
                    ScheduleDayFragment::class
                )
            )

            calendar.addOnChangeListener(object: OneRowCalendar.OnChangeListener {
                override fun onDayChange(newDay: Date?) {
                    tvDateNow.text = DateHelper.dateToString(newDay?: DateHelper.getCurrDate())
                }

                @SuppressLint("SetTextI18n")
                override fun onWeekChange(newWeekNum: Int) {
                    tvStudyWeek.text = "Учебная неделя №$newWeekNum"
                    User.group?.let { viewModel.changeWeekSchedule(it.id, newWeekNum) }
                }
            })

            calendar.setCountWeek(viewModel.countWeeks)

        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.weekScheduleLiveData.observe(this) {
            if (it.countWeek != viewModel.countWeeks) {
                viewModel.countWeeks = it.countWeek
                binding.calendar.setCountWeek(it.countWeek)
            }
        }
    }
}