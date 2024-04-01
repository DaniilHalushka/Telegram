package com.daniil.halushka.telegram.ui.screens.activities.authorization

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.databinding.ActivityAuthorizationBinding
import com.daniil.halushka.telegram.ui.screens.fragments.authorization.EnterPhoneNumberFragment
import com.daniil.halushka.telegram.util.initializeFirebase
import com.daniil.halushka.telegram.util.replaceFragment

class AuthorizationActivity : AppCompatActivity() {

    private lateinit var moduleBinding: ActivityAuthorizationBinding
    private lateinit var moduleToolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moduleBinding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(moduleBinding.root)
        initializeFirebase()
    }

    override fun onStart() {
        super.onStart()
        moduleToolbar = moduleBinding.authorizationToolbar
        setSupportActionBar(moduleToolbar)
        title = getString(R.string.register_title_your_phone)

        replaceFragment(EnterPhoneNumberFragment(), false)
    }
}