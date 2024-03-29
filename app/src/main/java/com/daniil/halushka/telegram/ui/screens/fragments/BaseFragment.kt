package com.daniil.halushka.telegram.ui.screens.fragments

import androidx.fragment.app.Fragment
import com.daniil.halushka.telegram.ui.screens.activities.main.MainActivity

open class BaseFragment(layout: Int) : Fragment(layout) {
    override fun onStart() {
        super.onStart()
        (activity as MainActivity).moduleAppDrawer.disableDrawer()
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).moduleAppDrawer.enableDrawer()
    }
}