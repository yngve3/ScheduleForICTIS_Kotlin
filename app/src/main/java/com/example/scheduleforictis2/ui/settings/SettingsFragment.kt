package com.example.scheduleforictis2.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.scheduleforictis2.R
import com.example.scheduleforictis2.utils.User

class SettingsFragment: PreferenceFragmentCompat() {

    private lateinit var vpkPref: Preference
    private lateinit var groupPref: Preference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = super.onCreateView(inflater, container, savedInstanceState)
        v.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black))
        return v
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        vpkPref = findPreference("pref_vpk_selection")!!
        groupPref = findPreference("pref_group_selection")!!

        with(vpkPref) {
            setOnPreferenceClickListener {
                findNavController().navigate(R.id.action_settingsScreen_to_groupSelectionFragment_VPK)
                true
            }
        }

        with(groupPref) {
            setOnPreferenceClickListener {
                findNavController().navigate(R.id.action_settingsScreen_to_groupSelectionFragment_Group)
                true
            }
        }

        updateUI()
    }

    //TODO Костыль. Переделать, сделать через PreferenceChangeListener
    private fun updateUI() {
        with(vpkPref) {
            if (User.group == null) {
                isVisible = false
            } else {
                isVisible = true
                User.vpk?.let {
                    summary = it.name
                } ?: run {
                    summary = "Выберете ВПК, расписание которого будет отображаться вместе с основным"
                }
            }
        }

        User.group?.let { groupPref.summary = it.name }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }
}