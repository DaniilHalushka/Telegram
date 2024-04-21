package com.daniil.halushka.telegram.ui.screens.fragments.chat

import androidx.fragment.app.Fragment
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.util.APP_ACTIVITY

class ChatFragment : Fragment(R.layout.fragment_chat) {
    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.telegram_title)
        APP_ACTIVITY.moduleAppDrawer.enableDrawer()
    }
}