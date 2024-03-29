package com.daniil.halushka.telegram.ui.screens.fragments.settings

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.ui.screens.activities.authorization.AuthorizationActivity
import com.daniil.halushka.telegram.ui.screens.activities.main.MainActivity
import com.daniil.halushka.telegram.ui.screens.fragments.BaseFragment
import com.daniil.halushka.telegram.util.AUTH
import com.daniil.halushka.telegram.util.replaceActivity

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {
    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_menu_exit -> {
                AUTH.signOut()
                (activity as MainActivity).replaceActivity(AuthorizationActivity())
            }
        }
        return true
    }
}