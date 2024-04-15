package com.daniil.halushka.telegram.ui.screens.fragments.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.data.models.CommonModel
import com.daniil.halushka.telegram.databinding.FragmentSingleChatBinding
import com.daniil.halushka.telegram.ui.screens.fragments.BaseFragment
import com.daniil.halushka.telegram.util.APP_ACTIVITY

class SingleChatFragment(contact: CommonModel) : BaseFragment(R.layout.fragment_single_chat) {

    private lateinit var singleChatBinding: FragmentSingleChatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        singleChatBinding = FragmentSingleChatBinding.inflate(inflater, container, false)
        return singleChatBinding.root
    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.moduleToolbar.findViewById<ConstraintLayout>(R.id.toolbar_info).visibility =
            View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        APP_ACTIVITY.moduleToolbar.findViewById<ConstraintLayout>(R.id.toolbar_info).visibility =
            View.GONE
    }
}