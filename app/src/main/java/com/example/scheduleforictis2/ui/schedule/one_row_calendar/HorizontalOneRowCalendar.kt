package com.example.scheduleforictis2.ui.schedule.one_row_calendar

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.scheduleforictis2.ui.models.Date
import com.example.scheduleforictis2.utils.DateHelper

class HorizontalOneRowCalendar(
    @LayoutRes selectedLayout: Int,
    @LayoutRes unselectedLayout: Int,
    private val recyclerView: RecyclerView,
    private val pager: ViewPager2,
    private val dateHelper: DateHelper,
    countWeeks: Int,
    private val listener: OnChangeListener
) {
    interface OnChangeListener {
        fun onWeekChange(newWeekNum: Int)
        fun onDayChange(newDay: Date?)
    }

    private val adapter: RecyclerAdapter
    var displayCountDaysOfWeek: Int = 6

    init {
        adapter = RecyclerAdapter(dateHelper.getDates(countWeeks), selectedLayout, unselectedLayout)
        init()
    }

    private fun init() {
        val snapHelper = SnapToBlock(displayCountDaysOfWeek)
        adapter.addItemSelectedListener { date ->
            listener.onDayChange(date)
            if (date != null) {
                changePagerPosition(date.dayOfWeek - 1)
            }
        }
        snapHelper.attachToRecyclerView(recyclerView)
        snapHelper.setSnapBlockCallback(object : SnapToBlock.SnapBlockCallback {
            override fun onBlockSnap(snapPosition: Int) {}
            override fun onBlockSnapped(snapPosition: Int) {
                listener.onWeekChange(snapPosition / displayCountDaysOfWeek + 1)
                changeCalendarPosition(snapPosition + numOfSelectedDayOfWeek, false)
            }
        })
        recyclerView.adapter = adapter
        recyclerView.reduceDragSensitivity(7)

        pager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                changeCalendarPosition(position - numOfSelectedDayOfWeek, true)
            }
        })
        toCurrentDay()
        listener.onWeekChange(dateHelper.getCurrWeek())
    }

    private fun toCurrentDay() {
        changeCalendarPosition(currentDayPosition, false)
        recyclerView.scrollToPosition(dateHelper.getCurrWeek() * displayCountDaysOfWeek - 1)
    }

    private val currentDayPosition: Int
        get() = ((dateHelper.getCurrDayOfWeek() - 1)
                + displayCountDaysOfWeek
                * (dateHelper.getCurrWeek() - 1))

    private fun changeCalendarPosition(position: Int, isOffset: Boolean) {
        if (!isOffset) {
            if (adapter.currSelectPosition != position) {
                adapter.selectElement(position)
            }
        } else {
            if (position != 0) adapter.scroll(position)
        }
    }

    private fun changePagerPosition(position: Int) {
        if (pager.currentItem != position) {
            pager.currentItem = position
        }
    }

    private val numOfSelectedDayOfWeek: Int
        get() {
            return adapter.currSelectPosition % displayCountDaysOfWeek
        }

    fun getDateHelper(): DateHelper {
        return dateHelper
    }

    fun updateDates(countWeeks: Int) {
        this.adapter.updateItems(DateHelper.getDates(countWeeks))
    }

    private fun RecyclerView.reduceDragSensitivity(f: Int) {
        val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
        touchSlopField.isAccessible = true
        val touchSlop = touchSlopField.get(this) as Int
        touchSlopField.set(this, touchSlop*f)
    }
}