package com.daniil.halushka.telegram.ui.screens.mainScreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.databinding.ActivityMainBinding
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var moduleDrawer: Drawer
    private lateinit var moduleHeader: AccountHeader
    private lateinit var moduleToolbar: Toolbar

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
    }

    private fun initializeFunctionality() {
        setSupportActionBar(moduleToolbar)
        createHeader()
        createDrawer()
    }

    private fun createHeader() {
        moduleHeader = AccountHeaderBuilder()
            .withActivity(this)
            .withHeaderBackground(R.drawable.header)
            .addProfiles(
                ProfileDrawerItem()
                    .withName("Daniil Halushka")
                    .withEmail("test@gmail.com")
            ).build()
    }

    private fun createDrawer(){
        moduleDrawer = DrawerBuilder()
            .withActivity(this)
            .withToolbar(moduleToolbar)
            .withAccountHeader(moduleHeader)
            .withActionBarDrawerToggle(true)
            .withSelectedItem(-1)
            .addDrawerItems(
                PrimaryDrawerItem()
                    .withIdentifier(100)
                    .withIconTintingEnabled(true)
                    .withName(R.string.create_group)
                    .withSelectable(false)
            ).build()
    }
}