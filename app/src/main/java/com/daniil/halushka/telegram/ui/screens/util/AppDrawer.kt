package com.daniil.halushka.telegram.ui.screens.util

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.ui.screens.fragments.settings.SettingsFragment
import com.daniil.halushka.telegram.util.replaceFragment
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem

class AppDrawer(
    val mainActivity: AppCompatActivity,
    private val toolbar: Toolbar
) {
    private lateinit var moduleDrawer: Drawer
    private lateinit var moduleHeader: AccountHeader
    private lateinit var moduleDrawerLayout: DrawerLayout

    fun create(){
        createHeader()
        createDrawer()
        moduleDrawerLayout = moduleDrawer.drawerLayout
    }

    fun disableDrawer(){
        moduleDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        moduleDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        toolbar.setNavigationOnClickListener {
            mainActivity.supportFragmentManager.popBackStack()
        }
    }

    fun enableDrawer(){
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        moduleDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = true
        moduleDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        toolbar.setNavigationOnClickListener {
            moduleDrawer.openDrawer()
        }
    }

    private fun createHeader() {
        moduleHeader = AccountHeaderBuilder()
            .withActivity(mainActivity)
            .withHeaderBackground(R.drawable.header)
            .addProfiles(
                ProfileDrawerItem()
                    .withName("Daniil Halushka")
                    .withEmail("test@gmail.com")
            ).build()
    }

    private fun createDrawer() {
        moduleDrawer = DrawerBuilder()
            .withActivity(mainActivity)
            .withToolbar(toolbar)
            .withAccountHeader(moduleHeader)
            .withActionBarDrawerToggle(true)
            .withSelectedItem(-1)
            .addDrawerItems(
                PrimaryDrawerItem()
                    .withIdentifier(100)
                    .withIconTintingEnabled(true)
                    .withName(R.string.create_group)
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_menu_create_groups),
                PrimaryDrawerItem()
                    .withIdentifier(101)
                    .withIconTintingEnabled(true)
                    .withName(R.string.create_secret_chat)
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_menu_secret_chat),
                PrimaryDrawerItem()
                    .withIdentifier(102)
                    .withIconTintingEnabled(true)
                    .withName(R.string.create_channel)
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_menu_create_channel),
                PrimaryDrawerItem()
                    .withIdentifier(103)
                    .withIconTintingEnabled(true)
                    .withName(R.string.contacts)
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_menu_contacts),
                PrimaryDrawerItem()
                    .withIdentifier(104)
                    .withIconTintingEnabled(true)
                    .withName(R.string.calls)
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_menu_phone),
                PrimaryDrawerItem()
                    .withIdentifier(105)
                    .withIconTintingEnabled(true)
                    .withName(R.string.saved_messages)
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_menu_favorites),
                PrimaryDrawerItem()
                    .withIdentifier(106)
                    .withIconTintingEnabled(true)
                    .withName(R.string.settings)
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_menu_settings),
                DividerDrawerItem(),
                PrimaryDrawerItem()
                    .withIdentifier(107)
                    .withIconTintingEnabled(true)
                    .withName(R.string.add_friends)
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_menu_invate),
                PrimaryDrawerItem()
                    .withIdentifier(108)
                    .withIconTintingEnabled(true)
                    .withName(R.string.faq)
                    .withSelectable(false)
                    .withIcon(R.drawable.ic_menu_help),
            ).withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                override fun onItemClick(
                    view: View?,
                    position: Int,
                    drawerItem: IDrawerItem<*>
                ): Boolean {
                    when (position) {
                        7 -> mainActivity.replaceFragment(SettingsFragment())
                    }
                    return false
                }
            }).build()
    }
}