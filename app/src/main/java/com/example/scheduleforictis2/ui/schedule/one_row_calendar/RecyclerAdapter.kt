package com.example.scheduleforictis2.ui.schedule.one_row_calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleforictis2.R
import com.example.scheduleforictis2.ui.models.Date
import com.example.scheduleforictis2.utils.DateHelper
import com.example.scheduleforictis2.utils.DiffUtilCallback

class RecyclerAdapter(
    private val items: MutableList<Date>,
    @LayoutRes private val selectedLayout: Int,
    @LayoutRes private val unselectedLayout: Int
) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    fun interface OnItemSelectedListener {
        fun onSelect(date: Date?)
    }

    private var listener: OnItemSelectedListener? = null

    fun addItemSelectedListener(listener: OnItemSelectedListener?) {
        this.listener = listener
    }

    fun updateItems(items: List<Date>) {
        items[currSelectPosition].isSelected = true
        val callback = DiffUtilCallback(this.items, items)
        val result = DiffUtil.calculateDiff(callback)
        result.dispatchUpdatesTo(this)
        this.items.clear()
        this.items.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout: Int = if (viewType == UNSELECTED) {
            unselectedLayout
        } else {
            selectedLayout
        }
        val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], this::selectElement)
    }

    private val UNSELECTED = 0
    private val SELECTED = 1

    override fun getItemViewType(position: Int): Int {
        return if (items[position].isSelected) SELECTED else UNSELECTED
    }

    var currSelectPosition = -1
        private set

    override fun getItemCount(): Int {
        return items.size
    }

    fun selectElement(newPosition: Int) {
        changeSelectedElement(newPosition)
        listener!!.onSelect(items[newPosition])
    }

    fun scroll(offset: Int) {
        changeSelectedElement(currSelectPosition + offset)
        listener!!.onSelect(items[currSelectPosition])
    }

    private fun changeSelectedElement(newPosition: Int) {
        if (currSelectPosition >= 0) {
            items[currSelectPosition].isSelected = false
            notifyItemChanged(currSelectPosition)
        }
        currSelectPosition = newPosition
        items[currSelectPosition].isSelected = true
        notifyItemChanged(currSelectPosition)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun interface OnItemClickListener {
            fun onItemClick(position: Int)
        }

        private val tvDayOfWeek: TextView
        private val tvDayOfMonth: TextView
        private val tvMonth: TextView

        init {
            tvDayOfWeek = itemView.findViewById(R.id.tvDayOfWeek)
            tvDayOfMonth = itemView.findViewById(R.id.tvDayOfMonth)
            tvMonth = itemView.findViewById(R.id.tvMonth)
        }

        fun bind(date: Date, listener: OnItemClickListener) {

            tvDayOfWeek.text = DateHelper.getWeekDayName(date.dayOfWeek, DateHelper.WeekdayDisplayMode.TWO_CHAR)
            tvDayOfMonth.text = date.dayOfMonth.toString()
            tvMonth.text = DateHelper.getMonthName(date.month, DateHelper.MonthDisplayMode.THREE_CHAR)

            itemView.setOnClickListener { listener.onItemClick(adapterPosition) }
        }
    }
}