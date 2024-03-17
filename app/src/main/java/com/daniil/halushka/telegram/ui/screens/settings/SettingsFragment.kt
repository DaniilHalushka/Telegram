package com.daniil.halushka.telegram.ui.screens.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.daniil.halushka.telegram.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var moduleBinding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        moduleBinding = FragmentSettingsBinding.inflate(layoutInflater)
        return moduleBinding.root
    }

    override fun onResume() {
        super.onResume()
    }


}