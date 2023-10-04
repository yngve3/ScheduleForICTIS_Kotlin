package com.example.scheduleforictis2.ui.group_selection

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleforictis2.R
import com.example.scheduleforictis2.network.models.GroupApi
import com.example.scheduleforictis2.utils.DiffUtilCallback

class GroupAdapter(private var groups: MutableList<GroupApi>, private val listener: OnItemSelectedListener) :
    RecyclerView.Adapter<GroupAdapter.ViewHolder>() {
    interface OnItemSelectedListener {
        fun onItemSelected(group: GroupApi)
    }

    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {

        fun interface OnItemClickListener {
            fun onItemClick(position: Int)
        }

        private val vpkName: TextView

        init {
            vpkName = v.findViewById(R.id.tvVpkName)
        }

        fun bind(group: GroupApi, listener: OnItemClickListener) {
            vpkName.text = group.name
            itemView.setOnClickListener { listener.onItemClick(adapterPosition) }
        }
    }

    fun updateItems(groups: List<GroupApi>) {
        val callback = DiffUtilCallback(this.groups, groups)
        val result = DiffUtil.calculateDiff(callback)
        result.dispatchUpdatesTo(this)
        this.groups.clear()
        this.groups.addAll(groups)
    }

    private val UNSELECTED = 0
    private val SELECTED = 1

    override fun getItemViewType(position: Int): Int {
        return if (groups[position].isSelected) SELECTED else UNSELECTED
    }

    private var currSelectPosition = 0

    private fun selectElement(newPosition: Int) {
        changeSelectedElement(newPosition)
        listener.onItemSelected(groups[newPosition])
    }

    private fun changeSelectedElement(newPosition: Int) {
        groups[currSelectPosition].isSelected = false
        notifyItemChanged(currSelectPosition)
        currSelectPosition = newPosition
        groups[currSelectPosition].isSelected = true
        notifyItemChanged(currSelectPosition)
    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout: Int = if (viewType == UNSELECTED) {
            R.layout.item_group_selection_unselected
        } else {
            R.layout.item_group_selection_selected
        }
        val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = groups.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(groups[position], this::selectElement)
    }
}