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

    var daySchedule: DaySchedule? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerScheduleAdapter = RecyclerScheduleAdapter(this)
        binding.rvSchedule.layoutManager = LinearLayoutManager(context)
        binding.rvSchedule.adapter = recyclerScheduleAdapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.weekScheduleLiveData.observe(viewLifecycleOwner) {
            daySchedule = it.getDay(positionFragment)
            recyclerScheduleAdapter!!.updateCouples(daySchedule!!.couples)
        }
    }

    override fun onItemClick(couple: Couple?) {

    }

    override fun onSelectVPK() {
        findNavController().navigate(R.id.action_scheduleScreen_to_groupSelectionScreen_VPK)
    }
}