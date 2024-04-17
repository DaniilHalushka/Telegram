package com.daniil.halushka.telegram.ui.screens.util

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.drawerlayout.widget.DrawerLayout
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.ui.screens.fragments.contacts.ContactsFragment
import com.daniil.halushka.telegram.ui.screens.fragments.settings.SettingsFragment
import com.daniil.halushka.telegram.util.APP_ACTIVITY
import com.daniil.halushka.telegram.util.USER
import com.daniil.halushka.telegram.util.downloadAndSetImage
import com.daniil.halushka.telegram.util.replaceFragment
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader

class AppDrawer {
    private lateinit var moduleDrawer: Drawer
    private lateinit var moduleHeader: AccountHeader
    private lateinit var moduleDrawerLayout: DrawerLayout
    private lateinit var moduleCurrentProfile: ProfileDrawerItem

    fun create() {
        initializeLoader()
        createHeader()
        createDrawer()
        moduleDrawerLayout = moduleDrawer.drawerLayout
    }

    fun disableDrawer() {
        moduleDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        moduleDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        APP_ACTIVITY.moduleToolbar.setNavigationOnClickListener {
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }
    }

    fun enableDrawer() {
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        moduleDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = true
        moduleDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        APP_ACTIVITY.moduleToolbar.setNavigationOnClickListener {
            moduleDrawer.openDrawer()
        }
    }

    private fun createHeader() {
        moduleCurrentProfile = ProfileDrawerItem()
            .withName(USER.fullname)
            .withEmail(USER.phone)
            .withIcon(USER.photoURL)
            .withIdentifier(100)
        moduleHeader = AccountHeaderBuilder()
            .withActivity(APP_ACTIVITY)
            .withHeaderBackground(R.drawable.header)
            .addProfiles(moduleCurrentProfile)
            .build()
    }

    fun updateHeader() {
        moduleCurrentProfile
            .withName(USER.fullname)
            .withEmail(USER.phone)
            .withIcon(USER.photoURL)

        moduleHeader.updateProfile(moduleCurrentProfile)
    }

    private fun initializeLoader() {
        DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable, tag: String?) {
                super.set(imageView, uri, placeholder, tag)
                imageView.downloadAndSetImage(uri.toString())
            }
        })
    }

    private fun createDrawer() {
        moduleDrawer = DrawerBuilder()
            .withActivity(APP_ACTIVITY)
            .withToolbar(APP_ACTIVITY.moduleToolbar)
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
                    clickToItem(position)
                    return false
                }
            }).build()
    }
}

private fun clickToItem(position: Int) {
    when (position) {
        4 -> APP_ACTIVITY.replaceFragment(ContactsFragment(), R.id.main_data_container)
        7 -> APP_ACTIVITY.replaceFragment(SettingsFragment(), R.id.main_data_container)
    }
}