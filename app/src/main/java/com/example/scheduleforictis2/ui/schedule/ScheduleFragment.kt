package com.example.scheduleforictis2.ui.schedule

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.scheduleforictis2.R
import com.example.scheduleforictis2.databinding.FragmentScheduleBinding
import com.example.scheduleforictis2.ui.MainViewModel
import com.example.scheduleforictis2.ui.models.Date
import com.example.scheduleforictis2.ui.schedule.one_row_calendar.HorizontalOneRowCalendar
import com.example.scheduleforictis2.ui.schedule.one_row_calendar.MaxCountLayoutManager
import com.example.scheduleforictis2.utils.DateHelper
import com.example.scheduleforictis2.utils.User
import java.time.LocalDate

class ScheduleFragment: Fragment() {

    private lateinit var binding: FragmentScheduleBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var horizontalOneRowCalendar: HorizontalOneRowCalendar
    private var countWeeks = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        if (countWeeks == 0) countWeeks = DateHelper.getCurrWeek()

        with(binding) {

            horizontalCalendar.layoutManager = MaxCountLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false,
                6
            )

            viewPager2.adapter = ViewPagerDaysOfWeekAdapter(childFragmentManager, lifecycle)
            viewPager2.reduceDragSensitivity(7)

            horizontalOneRowCalendar = HorizontalOneRowCalendar(
                R.layout.item_recycler_view_selected,
                R.layout.item_recycler_view_unselected,
                horizontalCalendar,
                viewPager2,
                DateHelper,
                countWeeks,
                object: HorizontalOneRowCalendar.OnChangeListener {
                    @SuppressLint("SetTextI18n")
                    override fun onWeekChange(newWeekNum: Int) {
                        tvStudyWeek.text = "Учебная неделя №$newWeekNum"
                        User.group?.let { viewModel.changeWeekSchedule(it.id, newWeekNum) }
                    }

                    override fun onDayChange(newDay: Date?) {
                        tvDateNow.text = DateHelper.dateToString(newDay?: Date(LocalDate.now()))
                    }
                }
            )
        }
    }

    private fun ViewPager2.reduceDragSensitivity(f: Int) {
        val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
        recyclerViewField.isAccessible = true
        val recyclerView = recyclerViewField.get(this) as RecyclerView

        val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
        touchSlopField.isAccessible = true
        val touchSlop = touchSlopField.get(recyclerView) as Int
        touchSlopField.set(recyclerView, touchSlop*f)
    }

    override fun onResume() {
        super.onResume()
        viewModel.weekScheduleLiveData.observe(this) {
            if (it.countWeek != this.countWeeks) {
                this.countWeeks = it.countWeek
                horizontalOneRowCalendar.updateDates(it.countWeek)
            }
        }
    }
}