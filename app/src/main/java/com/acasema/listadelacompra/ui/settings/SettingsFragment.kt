package com.acasema.listadelacompra.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.acasema.listadelacompra.R
import com.acasema.listadelacompra.ui.controller.FabController
import com.acasema.listadelacompra.ui.main.MainActivity
import com.acasema.listadelacompra.ui.signin.SignInViewModel

/**
 * autor: acasema (alfonso)
 *  clase derivada de PreferenceFragmentCompat: configuración
 */
class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {


    private lateinit var viewModel: SettingsViewModel
    private val PREFERENCIA_USUARIO = "Username"

    override fun onResume() {
        super.onResume()

        val a: FabController = (activity as MainActivity).fabController
        a.setVisibility(false)

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this)

    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
        viewModel.getUserName()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel.getUserNameLiveData().observe(viewLifecycleOwner, {
            val sp :SharedPreferences = getPreferenceScreen().getSharedPreferences();
            val editTextPref: EditTextPreference? = findPreference(PREFERENCIA_USUARIO);
            editTextPref!!.setSummary(sp.getString(PREFERENCIA_USUARIO, it));
        })
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPref: SharedPreferences, key: String) {
        if (key == PREFERENCIA_USUARIO) {
            viewModel.rename(sharedPref.getString(key, "")!!)
        }
    }
}