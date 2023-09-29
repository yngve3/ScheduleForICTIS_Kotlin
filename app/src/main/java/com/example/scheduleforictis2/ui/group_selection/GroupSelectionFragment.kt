package com.example.scheduleforictis2.ui.group_selection

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.scheduleforictis2.databinding.FragmentGroupSelectionBinding
import com.example.scheduleforictis2.network.models.GroupApi
import com.example.scheduleforictis2.ui.MainActivity
import com.example.scheduleforictis2.ui.MainViewModel
import com.example.scheduleforictis2.utils.ParserModels.asGroup
import com.example.scheduleforictis2.utils.ext.hideKeyboard
import com.example.scheduleforictis2.utils.ext.showKeyboard

class GroupSelectionFragment: Fragment(), GroupAdapter.OnItemSelectedListener {

    companion object {
        private const val isVPKName = "isVPK"
        private const val isFirstName = "isFirst"

        fun newInstance(isVPK: Boolean, isFirst: Boolean): Fragment {
            val args = Bundle()
            args.putBoolean(isVPKName, isVPK)
            args.putBoolean(isFirstName, isFirst)

            val fragment = GroupSelectionFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var binding: FragmentGroupSelectionBinding
    private lateinit var viewModel: MainViewModel
    private var selectedGroup: GroupApi? = null
    private lateinit var adapter: GroupAdapter
    private var isVPK: Boolean = false
    private var isFirst: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)

        isVPK = requireArguments().getBoolean(isVPKName)
        isFirst = requireArguments().getBoolean(isFirstName)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if (isVPK) {
            binding.groupSelectionEtSearch.visibility = View.GONE
            binding.groupSelectionToolbar.title = "Выбор ВПК"
        } else {
            binding.groupSelectionToolbar.title = "Выбор группы"
            with (binding.groupSelectionEtSearch) {
                editText?.showKeyboard()
                editText?.setOnEditorActionListener { v, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        binding.groupSelectionBtnSave.enable(false)
                        viewModel.search(editText?.text.toString().trim()).observe(viewLifecycleOwner) {
                            if (it != null) {
                                adapter.updateItems(it)
                            } else {
                                Toast.makeText(requireContext(), "Нет таких", Toast.LENGTH_SHORT).show()
                            }

                        }
                    }
                    false
                }
            }
        }

        adapter = GroupAdapter(mutableListOf(), this@GroupSelectionFragment)

        with(binding.rvGroupList) {
            adapter = this@GroupSelectionFragment.adapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }

        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        (requireActivity() as MainActivity).setSupportActionBar(binding.groupSelectionToolbar)
        (requireActivity() as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(!isFirst)

        with(binding.groupSelectionBtnSave) {
            setOnClickListener {
                binding.groupSelectionEtSearch.editText?.hideKeyboard()
                viewModel.saveGroup(selectedGroup!!.asGroup(), isVPK)
                findNavController().popBackStack()
            }
            enable(false)
        }
    }

    private fun Button.enable(isEnable: Boolean) {
        this.isEnabled = isEnable

        if (isEnable) {
            this.alpha = 1f
        } else {
            this.alpha = 0.2f
        }
    }

    override fun onItemSelected(group: GroupApi) {
        selectedGroup = group
        binding.groupSelectionBtnSave.enable(true)
    }

    override fun onResume() {
        super.onResume()

        if (isVPK) {
            viewModel.search("ВПК").observe(viewLifecycleOwner) {
                adapter.updateItems(it)
            }
        } else {
            binding.groupSelectionEtSearch.requestFocus()
        }
    }

}