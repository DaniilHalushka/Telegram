package com.daniil.halushka.telegram.ui.screens.fragments.base

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.util.APP_ACTIVITY
import com.daniil.halushka.telegram.util.hideKeyboard

open class BaseChangeFragment(layout: Int) : Fragment(layout) {
    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
        APP_ACTIVITY.moduleAppDrawer.disableDrawer()
        hideKeyboard()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        APP_ACTIVITY.menuInflater.inflate(R.menu.settings_confirm_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_confirm_change -> change()
        }
        return true
    }

    open fun change() {

    }
}