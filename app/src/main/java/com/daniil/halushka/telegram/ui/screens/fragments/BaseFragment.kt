package com.daniil.halushka.telegram.ui.screens.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

open class BaseFragment(
    private val layout: Int
) : Fragment(layout) {
    private lateinit var moduleRootView: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        moduleRootView = inflater.inflate(layout, container, false)
        return moduleRootView
    }

    override fun onStart() {
        super.onStart()
    }
}