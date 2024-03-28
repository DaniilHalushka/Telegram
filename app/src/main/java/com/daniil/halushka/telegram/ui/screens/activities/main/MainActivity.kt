package com.daniil.halushka.telegram.ui.screens.activities.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.daniil.halushka.telegram.databinding.ActivityMainBinding
import com.daniil.halushka.telegram.ui.screens.activities.authorization.AuthorizationActivity
import com.daniil.halushka.telegram.ui.screens.fragments.chat.ChatFragment
import com.daniil.halushka.telegram.ui.screens.util.AppDrawer
import com.daniil.halushka.telegram.util.AUTH
import com.daniil.halushka.telegram.util.initializeFirebase
import com.daniil.halushka.telegram.util.replaceActivity
import com.daniil.halushka.telegram.util.replaceFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var moduleToolbar: Toolbar

    private lateinit var moduleAppDrawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        initializeFields()
        initializeFunctionality()
    }

    private fun initializeFields() {
        moduleToolbar = binding.mainToolbar
        moduleAppDrawer = AppDrawer(
            mainActivity = this,
            toolbar = moduleToolbar
        )
        initializeFirebase()
    }

    private fun initializeFunctionality() {
        if (AUTH.currentUser != null) {
            moduleAppDrawer.create()
            setSupportActionBar(moduleToolbar)
            replaceFragment(ChatFragment(), false)
        } else {
            replaceActivity(AuthorizationActivity())
        }
    }
}