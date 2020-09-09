package com.destack.hwmonitor.fragments

import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.destack.hwmonitor.R


class SettingsFragment : PreferenceFragmentCompat() {

    /**
     * Called during onCreate(Bundle) to supply the preferences for this
     * fragment. This calls setPreferenceFromResource to get the preferences
     * from the XML file.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     * @param rootKey            If non-null, this preference fragment
     *                           should be rooted with this key.
     */
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        // Set attributes programmatically since setting it in XML does not work
        val editTextPrefIP = preferenceManager.findPreference<EditTextPreference>("server_ip")
        editTextPrefIP?.setOnBindEditTextListener { editText ->
            editText.isSingleLine = true
        }

        val editTextPrefPort = preferenceManager.findPreference<EditTextPreference>("server_port")
        editTextPrefPort?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
            editText.filters += InputFilter.LengthFilter(5)
        }
    }

}