package com.daniil.halushka.telegram.ui.screens.fragments

import androidx.fragment.app.Fragment
import com.daniil.halushka.telegram.util.APP_ACTIVITY

open class BaseFragment(layout: Int) : Fragment(layout) {
    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.moduleAppDrawer.disableDrawer()
    }
}