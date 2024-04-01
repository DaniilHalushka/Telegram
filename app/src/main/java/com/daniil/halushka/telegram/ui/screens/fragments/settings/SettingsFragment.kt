package com.daniil.halushka.telegram.ui.screens.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.databinding.FragmentSettingsBinding
import com.daniil.halushka.telegram.ui.screens.activities.authorization.AuthorizationActivity
import com.daniil.halushka.telegram.ui.screens.activities.main.MainActivity
import com.daniil.halushka.telegram.ui.screens.fragments.BaseFragment
import com.daniil.halushka.telegram.util.AUTH
import com.daniil.halushka.telegram.util.USER
import com.daniil.halushka.telegram.util.replaceActivity
import com.daniil.halushka.telegram.util.replaceParentFragment

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private lateinit var settingsBinding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsBinding = FragmentSettingsBinding.inflate(inflater, container, false)
        return settingsBinding.root
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        initializeFields()
    }

    private fun initializeFields() {
        settingsBinding.settingsFullName.text = USER.fullname
        settingsBinding.settingsStatus.text = USER.status
        settingsBinding.settingsPhoneNumber.text = USER.phone
        settingsBinding.settingsUsername.text = USER.username
        settingsBinding.settingsInformation.text = USER.information
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

            R.id.settings_menu_change_name -> replaceParentFragment(ChangeNameFragment())
        }
        return true
    }
}