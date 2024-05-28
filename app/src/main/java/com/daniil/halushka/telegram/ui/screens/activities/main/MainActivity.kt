package com.daniil.halushka.telegram.ui.screens.activities.main

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.database.AUTH
import com.daniil.halushka.telegram.database.initializeFirebase
import com.daniil.halushka.telegram.database.initializeUser
import com.daniil.halushka.telegram.databinding.ActivityMainBinding
import com.daniil.halushka.telegram.ui.screens.fragments.authorization.EnterPhoneNumberFragment
import com.daniil.halushka.telegram.ui.screens.fragments.main_list.MainListFragment
import com.daniil.halushka.telegram.ui.screens.util.AppDrawer
import com.daniil.halushka.telegram.util.APP_ACTIVITY
import com.daniil.halushka.telegram.util.AppStates
import com.daniil.halushka.telegram.util.READ_CONTACTS
import com.daniil.halushka.telegram.util.initializeContacts
import com.daniil.halushka.telegram.util.replaceFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var moduleToolbar: Toolbar
    lateinit var moduleAppDrawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        APP_ACTIVITY = this
        initializeFirebase()
        initializeUser {
            CoroutineScope(Dispatchers.IO).launch {
                initializeContacts()
            }
            initializeFields()
            initializeFunctionality()
        }
    }

    override fun onStart() {
        super.onStart()
        AppStates.updateState(AppStates.ONLINE)
    }

    override fun onStop() {
        super.onStop()
        AppStates.updateState(AppStates.OFFLINE)
    }

    private fun initializeFields() {
        moduleToolbar = binding.mainToolbar
        moduleAppDrawer = AppDrawer()
    }

    private fun initializeFunctionality() {
        setSupportActionBar(moduleToolbar)
        if (AUTH.currentUser != null) {
            moduleAppDrawer.create()
            replaceFragment(MainListFragment(), R.id.main_data_container, false)
        } else {
            replaceFragment(EnterPhoneNumberFragment(), R.id.main_data_container, false)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(
                APP_ACTIVITY,
                READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            initializeContacts()
        }
    }
}