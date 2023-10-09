package com.example.scheduleforictis2.ui.schedule

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scheduleforictis2.ui.schedule.one_row_calendar.PageFragment
import com.example.scheduleforictis2.R
import com.example.scheduleforictis2.databinding.FragmentCouplesListBinding
import com.example.scheduleforictis2.ui.MainViewModel
import com.example.scheduleforictis2.ui.models.Couple
import com.example.scheduleforictis2.ui.models.DaySchedule
import com.example.scheduleforictis2.ui.models.WeekSchedule

class ScheduleDayFragment: PageFragment(), RecyclerScheduleAdapter.OnItemClickListener {

    private val viewModel: MainViewModel by activityViewModels()
    private var recyclerScheduleAdapter: RecyclerScheduleAdapter? = null
    private lateinit var binding: FragmentCouplesListBinding
    private var positionFragment: Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        positionFragment = requireArguments().getInt(positionFragmentName)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCouplesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerScheduleAdapter = RecyclerScheduleAdapter(mutableListOf(), this, requireContext())
        binding.rvSchedule.layoutManager = LinearLayoutManager(context)
        binding.rvSchedule.adapter = recyclerScheduleAdapter

        binding.btnSelectVPK.setOnClickListener {
            findNavController().navigate(R.id.action_scheduleScreen_to_groupSelectionScreen_VPK)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.weekScheduleLiveData.observe(viewLifecycleOwner) {
            val daySchedule = it.getDay(positionFragment)
            initt(daySchedule)
            recyclerScheduleAdapter!!.updateCouples(daySchedule.couples)
        }
    }

    fun initt(daySchedule: DaySchedule) {
        with(daySchedule) {
            if (isBlank and !isWar and !isVPK) {
                showTovarish()
            } else if (isWar) {
                showMother()
            } else if (isVPK and !vpkIsChosen) {
                showVPK()
            } else if (vpkIsChosen) {
                showList()
            } else if (!isVPK and !isBlank) {
                showList()
            }
        }
    }

    private fun View.show(isVisible: Boolean) {
        if (isVisible and (this.visibility == View.GONE)) {
            this.visibility = View.VISIBLE
        }
        if (!isVisible and (this.visibility == View.VISIBLE)) {
            this.visibility = View.GONE
        }
    }

    private fun showTovarish() {
        with(binding) {
            btnSelectVPK.show(false)
            rvSchedule.show(false)
            ivMother.show(false)
            tovarish.show(true)
        }
    }

    private fun showMother() {
        with(binding) {
            btnSelectVPK.show(false)
            rvSchedule.show(true)
            tovarish.show(false)
            ivMother.show(true)
        }
    }

    private fun showList() {
        with(binding) {
            btnSelectVPK.show(false)
            ivMother.show(false)
            tovarish.show(false)
            rvSchedule.show(true)
        }
    }

    private fun showVPK() {
        with(binding) {
            rvSchedule.show(false)
            ivMother.show(false)
            tovarish.show(false)
            btnSelectVPK.show(true)
        }
    }

    override fun onItemClick(couple: Couple?) {

    }
}