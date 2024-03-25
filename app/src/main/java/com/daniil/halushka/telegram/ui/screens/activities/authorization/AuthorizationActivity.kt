package com.daniil.halushka.telegram.ui.screens.activities.authorization

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.daniil.halushka.telegram.databinding.ActivityAuthorizationBinding

class AuthorizationActivity : AppCompatActivity() {

    private lateinit var moduleBinding: ActivityAuthorizationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moduleBinding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(moduleBinding.root)
    }
}