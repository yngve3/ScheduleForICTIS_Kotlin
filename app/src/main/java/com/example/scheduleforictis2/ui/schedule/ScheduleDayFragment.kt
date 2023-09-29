package com.example.scheduleforictis2.ui.schedule

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scheduleforictis2.R
import com.example.scheduleforictis2.databinding.FragmentCouplesListBinding
import com.example.scheduleforictis2.ui.MainViewModel
import com.example.scheduleforictis2.ui.models.Couple
import com.example.scheduleforictis2.ui.models.DaySchedule

class ScheduleDayFragment: Fragment(), RecyclerScheduleAdapter.OnItemClickListener {

    companion object {
        private const val positionFragmentName = "positionFragment"

        fun newInstance(positionFragment: Int): ScheduleDayFragment {
            val args = Bundle()
            args.putInt(positionFragmentName, positionFragment)
            val fragment = ScheduleDayFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var viewModel: MainViewModel
    private var recyclerScheduleAdapter: RecyclerScheduleAdapter? = null
    private lateinit var binding: FragmentCouplesListBinding
    private var daySchedule: DaySchedule? = null
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

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
            daySchedule = it.getDay(positionFragment)

            with(daySchedule!!) {
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

                recyclerScheduleAdapter!!.updateCouples(couples)
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