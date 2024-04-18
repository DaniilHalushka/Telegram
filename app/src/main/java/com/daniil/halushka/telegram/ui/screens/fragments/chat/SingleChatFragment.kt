package com.daniil.halushka.telegram.ui.screens.fragments.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.data.models.CommonModel
import com.daniil.halushka.telegram.data.models.UserModel
import com.daniil.halushka.telegram.databinding.FragmentSingleChatBinding
import com.daniil.halushka.telegram.ui.screens.fragments.BaseFragment
import com.daniil.halushka.telegram.util.APP_ACTIVITY
import com.daniil.halushka.telegram.util.AppValueEventListener
import com.daniil.halushka.telegram.util.NODE_USERS
import com.daniil.halushka.telegram.util.REF_DATABASE_ROOT
import com.daniil.halushka.telegram.util.downloadAndSetImage
import com.daniil.halushka.telegram.util.getUserModel
import com.google.firebase.database.DatabaseReference

class SingleChatFragment(private val contact: CommonModel) :
    BaseFragment(R.layout.fragment_single_chat) {

    private lateinit var moduleListenerInfoToolbar: AppValueEventListener
    private lateinit var moduleReceivingUser: UserModel
    private lateinit var moduleToolbarInfo: View
    private lateinit var singleChatBinding: FragmentSingleChatBinding
    private lateinit var moduleRefUsers: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        singleChatBinding = FragmentSingleChatBinding.inflate(inflater, container, false)
        return singleChatBinding.root
    }

    override fun onResume() {
        super.onResume()
        moduleToolbarInfo =
            APP_ACTIVITY.moduleToolbar.findViewById<ConstraintLayout>(R.id.toolbar_info)
        moduleToolbarInfo.visibility = View.VISIBLE
        moduleListenerInfoToolbar = AppValueEventListener {
            moduleReceivingUser = it.getUserModel()
            initializeInfoToolbar()
        }

        moduleRefUsers = REF_DATABASE_ROOT.child(NODE_USERS).child(contact.id)
        moduleRefUsers.addValueEventListener(moduleListenerInfoToolbar)
    }

    private fun initializeInfoToolbar() {
        moduleToolbarInfo.findViewById<ImageView>(R.id.toolbar_chat_image)
            .downloadAndSetImage(moduleReceivingUser.photoURL)
        moduleToolbarInfo.findViewById<TextView>(R.id.toolbar_chat_fullname).text =
            moduleReceivingUser.fullname
        moduleToolbarInfo.findViewById<TextView>(R.id.toolbar_chat_status).text =
            moduleReceivingUser.state
    }

    override fun onPause() {
        super.onPause()
        moduleToolbarInfo.visibility = View.GONE
        moduleRefUsers.removeEventListener(moduleListenerInfoToolbar)
    }
}