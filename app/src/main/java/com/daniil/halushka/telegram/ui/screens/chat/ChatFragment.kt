package com.daniil.halushka.telegram.ui.screens.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.daniil.halushka.telegram.databinding.FragmentChatBinding


class ChatFragment : Fragment() {
    private lateinit var moduleBinding: FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        moduleBinding = FragmentChatBinding.inflate(layoutInflater)
        return moduleBinding.root
    }

    override fun onResume() {
        super.onResume()
    }
}