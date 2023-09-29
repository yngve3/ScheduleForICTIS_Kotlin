package com.example.scheduleforictis2.ui.schedule

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleforictis2.R
import com.example.scheduleforictis2.ui.models.Couple
import com.example.scheduleforictis2.utils.DiffUtilCallback

class RecyclerScheduleAdapter(
    private var couples: MutableList<Couple>,
    private val listener: OnItemClickListener,
    private val context: Context
) : RecyclerView.Adapter<RecyclerScheduleAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(couple: Couple?)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val tvTimeStart: TextView
        private val tvTimeEnd: TextView
        private val tvNameOfCouple: TextView
        private val tvAudience: TextView
        private val tvProfessor: TextView
        private val ivIndicator: View

        init {
            tvTimeStart = v.findViewById(R.id.tvTimeStart)
            tvTimeEnd = v.findViewById(R.id.tvTimeEnd)
            tvNameOfCouple = v.findViewById(R.id.tvNameOfCouple)
            tvAudience = v.findViewById(R.id.tvAudience)
            tvProfessor = v.findViewById(R.id.tvProfessor)
            ivIndicator = v.findViewById(R.id.ivIndicator)

        }

        @SuppressLint("ResourceAsColor")
        fun bind(couple: Couple, listener: OnItemClickListener, context: Context) {
            tvTimeStart.text = couple.getTimeStart()
            tvTimeEnd.text = couple.getTimeEnd()
            tvNameOfCouple.text = couple.discipline
            tvAudience.text = couple.audience
            tvProfessor.text = couple.professor
            if (couple.isOnline) {
                ivIndicator.setBackgroundResource(R.drawable.item_couples_list_indicator_green)
            } else {
                ivIndicator.setBackgroundResource(R.drawable.item_couples_list_indicator_red)
            }

            itemView.setOnClickListener { listener.onItemClick(couple) }
        }
    }

    fun updateCouples(couples: List<Couple>) {
        val callback = DiffUtilCallback(this.couples, couples)
        val result = DiffUtil.calculateDiff(callback)
        result.dispatchUpdatesTo(this)
        this.couples.clear()
        this.couples.addAll(couples)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_couples_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(couples[position], listener, context)
    }

    override fun getItemCount(): Int {
        return couples.size
    }
}