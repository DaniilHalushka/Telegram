package com.daniil.halushka.telegram.ui.screens.activities.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.daniil.halushka.telegram.data.models.User
import com.daniil.halushka.telegram.databinding.ActivityMainBinding
import com.daniil.halushka.telegram.ui.screens.activities.authorization.AuthorizationActivity
import com.daniil.halushka.telegram.ui.screens.fragments.chat.ChatFragment
import com.daniil.halushka.telegram.ui.screens.util.AppDrawer
import com.daniil.halushka.telegram.util.APP_ACTIVITY
import com.daniil.halushka.telegram.util.AUTH
import com.daniil.halushka.telegram.util.AppValueEventListener
import com.daniil.halushka.telegram.util.NODE_USERS
import com.daniil.halushka.telegram.util.REF_DATABASE_ROOT
import com.daniil.halushka.telegram.util.CURRENT_UID
import com.daniil.halushka.telegram.util.USER
import com.daniil.halushka.telegram.util.initializeFirebase
import com.daniil.halushka.telegram.util.replaceActivity
import com.daniil.halushka.telegram.util.replaceFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var moduleToolbar: Toolbar

    lateinit var moduleAppDrawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY = this
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
        initializeUser()
    }

    private fun initializeUser() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
            .addListenerForSingleValueEvent(
                AppValueEventListener {
                    USER = it.getValue(User::class.java) ?: User()
                }
            )
    }

    private fun initializeFunctionality() {
        if (AUTH.currentUser != null) {
            setSupportActionBar(moduleToolbar)
            moduleAppDrawer.create()
            replaceFragment(ChatFragment(), false)
        } else {
            replaceActivity(AuthorizationActivity())
        }
    }
}