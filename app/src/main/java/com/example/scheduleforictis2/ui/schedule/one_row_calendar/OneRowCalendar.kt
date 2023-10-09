package com.example.scheduleforictis2.ui.schedule.one_row_calendar

import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.scheduleforictis2.R
import kotlinx.coroutines.supervisorScope

class OneRowCalendar(
    context: Context,
    attrs: AttributeSet
): LinearLayout(context, attrs) {

    //TODO Переделать логику переключений и свойства, считающие всякое

    interface OnChangeListener {
        fun onWeekChange(newWeekNum: Int)
        fun onDayChange(newDay: Date?)
    }

    private var listener: OnChangeListener? = null

    private var viewPager2: ViewPager2
    private var recyclerView: RecyclerView
    private var oneRowAdapter: OneRowAdapter

    private var displayCountDaysOfWeek: Int = 6

    private var currentWeek = -1
    private val currentWeekPosition: Int
        get() = displayCountDaysOfWeek * currentWeek - 1

    private val currentDayPosition: Int
        get() = ((DateHelper.getCurrDate().dayOfWeek - 1)
                + displayCountDaysOfWeek
                * (DateHelper.getCurrDate().weekNum - 1))

    private val numOfSelectedDayOfWeek: Int
        get() {
            return oneRowAdapter.currentElement % displayCountDaysOfWeek
        }

    private var countWeeks: Int = -1

    fun addViewPagerAdapter(viewPagerDaysOfWeekAdapter: ViewPagerDaysOfWeekAdapter) {
        viewPager2.adapter = viewPagerDaysOfWeekAdapter
        viewPager2.currentItem = DateHelper.getCurrDate().dayOfWeek - 1
    }

    fun addOnChangeListener(listener: OnChangeListener) {
        this.listener = listener
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putInt("CurrentWeek", currentWeek)
        bundle.putInt("SelectedDay", oneRowAdapter.currentElement)
        bundle.putInt("CountWeeks", countWeeks)
        return bundle
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onRestoreInstanceState(state: Parcelable?) {
        var superState = state
        if (state is Bundle) {
            val bundle = state
            initUI(bundle)
            superState = bundle.getParcelable("superState", Parcelable::class.java)
        }
        super.onRestoreInstanceState(superState)
    }

    private fun initUI(state: Bundle?) {
        if (state == null) {
            oneRowAdapter.selectElement(currentDayPosition)
            currentWeek = DateHelper.getCurrDate().weekNum
            recyclerView.scrollToPosition(currentWeekPosition)
            this.listener?.onWeekChange(currentWeek)
        } else {
            setCountWeek(state.getInt("CountWeeks"))
            oneRowAdapter.selectElement(state.getInt("SelectedDay"))
            currentWeek = state.getInt("CurrentWeek")
            this.listener?.onWeekChange(currentWeek)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (currentWeek == -1) {
            initUI(null)
        }

        super.onLayout(changed, l, t, r, b)
    }

    init {
        val root = LayoutInflater.from(context).inflate(R.layout.one_row_calendar, this)

        val attributeArray: TypedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.OneRowCalendar, 0, 0
        )
        viewPager2 = root.findViewById(R.id.viewPager2)
        recyclerView = root.findViewById(R.id.horizontalCalendar)
//        oneRowAdapter = OneRowAdapter(
//            DateHelper.getDates(attributeArray.getInt(R.styleable.OneRowCalendar_countWeeks, 1)),
//            attributeArray.getResourceId(R.styleable.OneRowCalendar_layout_selected, R.layout.item_calendar_selected),
//            attributeArray.getResourceId(R.styleable.OneRowCalendar_layout_unselected, R.layout.item_calendar_unselected)
//        )

        oneRowAdapter = OneRowAdapter(
            mutableListOf(),
            R.layout.item_calendar_selected,
            R.layout.item_calendar_unselected
        )

        val snapHelper = SnapToBlock(displayCountDaysOfWeek)
        snapHelper.setSnapBlockCallback(object : SnapToBlock.SnapBlockCallback {
            override fun onBlockSnap(snapPosition: Int) {}
            override fun onBlockSnapped(snapPosition: Int) {
                currentWeek = snapPosition / displayCountDaysOfWeek + 1
                oneRowAdapter.selectElement(snapPosition + numOfSelectedDayOfWeek)
                listener?.onWeekChange(currentWeek)
            }
        })

        viewPager2.registerOnPageChangeCallback(object: OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                changeCalendarPosition(position)
            }
        })

        viewPager2.reduceDragSensitivity(5)
        viewPager2.offscreenPageLimit = 6

        oneRowAdapter.addItemSelectedListener {
            changePagerPosition(it!!.dayOfWeek - 1)
            listener?.onDayChange(it)
        }

        recyclerView.layoutManager = MaxCountLayoutManager(context, ORIENTATION_HORIZONTAL, false, 6)
        recyclerView.adapter = oneRowAdapter

        snapHelper.attachToRecyclerView(recyclerView)
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

    fun scrollBlock(countWeeks: Int) {
        currentWeek += countWeeks
        recyclerView.smoothScrollToPosition(currentWeekPosition)
    }

    fun changeCalendarPosition(position: Int) {
        oneRowAdapter.selectElementWithOffset(position - numOfSelectedDayOfWeek)
    }

    private fun changePagerPosition(position: Int) {
        if (position != viewPager2.currentItem) {
            viewPager2.currentItem = position
        }
    }

    fun setCountWeek(countWeeks: Int) {
        oneRowAdapter.updateItems(DateHelper.getDates(countWeeks))
        this.countWeeks = countWeeks
    }
}