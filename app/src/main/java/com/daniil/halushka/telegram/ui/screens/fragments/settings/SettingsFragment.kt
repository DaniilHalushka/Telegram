package com.daniil.halushka.telegram.ui.screens.fragments.settings

import android.view.Menu
import android.view.MenuInflater
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.ui.screens.fragments.BaseFragment

class SettingsFragment : BaseFragment(
    R.layout.fragment_settings
) {
    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }
}