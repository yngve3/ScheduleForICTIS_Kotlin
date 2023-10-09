package com.example.scheduleforictis2.ui.schedule

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleforictis2.R
import com.example.scheduleforictis2.ui.models.Couple
import com.example.scheduleforictis2.utils.DiffUtilCallback

class RecyclerScheduleAdapter(
    private val listener: OnItemClickListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(couple: Couple?)
        fun onSelectVPK()
    }

    private val items = mutableListOf<ScheduleItem>()

    class CoupleViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val tvTimeStart: TextView
        private val tvTimeEnd: TextView
        private val tvNameOfCouple: TextView
        private val tvAudience: TextView
        private val tvProfessor: TextView
        private val tvKindOfCouple: TextView
        private val ivIndicator: View

        init {
            tvTimeStart = v.findViewById(R.id.tvTimeStart)
            tvTimeEnd = v.findViewById(R.id.tvTimeEnd)
            tvNameOfCouple = v.findViewById(R.id.tvNameOfCouple)
            tvAudience = v.findViewById(R.id.tvAudience)
            tvProfessor = v.findViewById(R.id.tvProfessor)
            ivIndicator = v.findViewById(R.id.ivIndicator)
            tvKindOfCouple = v.findViewById(R.id.tvKindOfCouple)
        }

        @SuppressLint("ResourceAsColor")
        fun bind(scheduleItem: ScheduleItem, listener: OnItemClickListener) {
            val couple = scheduleItem.couple!!
            tvTimeStart.text = couple.getTimeStart()
            tvTimeEnd.text = couple.getTimeEnd()
            tvNameOfCouple.text = couple.discipline
            tvAudience.text = couple.audience
            tvProfessor.text = couple.professor

            ivIndicator.setBackgroundResource(
                if (couple.isOnline) {
                    R.drawable.item_couples_list_indicator_green
                } else {
                    R.drawable.item_couples_list_indicator_red
                }
            )

            //TODO Брать из ресурсов
            val kinds = arrayOf("ПР", "ЛАБ", "ЛЕК", "ЭКЗ", "")

            tvKindOfCouple.text = kinds[couple.kind.ordinal]

            itemView.setOnClickListener { listener.onItemClick(couple) }
        }
    }
    class AddVpkViewHolder(v: View): RecyclerView.ViewHolder(v) {
        private val btnSelectVPK: Button
        init {
            btnSelectVPK = v.findViewById(R.id.btnSelectVPK)
        }

        fun bind(listener: OnItemClickListener) {
            btnSelectVPK.setOnClickListener { listener.onSelectVPK() }
        }
    }
    class MessageViewHolder(v: View): RecyclerView.ViewHolder(v)

    fun updateCouples(couples: List<ScheduleItem>) {
        val callback = DiffUtilCallback(this.items, couples)
        val result = DiffUtil.calculateDiff(callback)
        result.dispatchUpdatesTo(this)
        this.items.clear()
        this.items.addAll(couples)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            ScheduleItem.ScheduleItemType.COUPLE.ordinal -> {
                return CoupleViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_couples_list_couple, parent, false)
                )
            }
            ScheduleItem.ScheduleItemType.ADD_VPK.ordinal -> {
                return AddVpkViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_couples_list_add_vpk, parent, false)
                )
            }
            ScheduleItem.ScheduleItemType.TOVARISH.ordinal -> {
                return MessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_couples_list_tovarish, parent, false)
                )
            }
            ScheduleItem.ScheduleItemType.MOTHER.ordinal -> {
                return MessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_couples_list_mother, parent, false)
                )
            }
            else -> {
                return MessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_couples_list_error, parent, false)
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type.ordinal
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CoupleViewHolder) {
            holder.bind(items[position], listener)
        }
        if (holder is AddVpkViewHolder) {
            holder.bind(listener)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}