package com.example.scheduleforictis2.ui.settings

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.scheduleforictis2.R

class SettingsFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        findPreference<Preference>("pref_vpk_selection")?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.action_settingsScreen_to_groupSelectionFragment_VPK)
            true
        }
        findPreference<Preference>("pref_group_selection")?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.action_settingsScreen_to_groupSelectionFragment_Group)
            true
        }
    }
}