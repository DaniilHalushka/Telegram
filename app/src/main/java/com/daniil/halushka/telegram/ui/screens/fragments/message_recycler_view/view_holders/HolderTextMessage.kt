package com.daniil.halushka.telegram.ui.screens.fragments.message_recycler_view.view_holders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.daniil.halushka.telegram.databinding.MessageItemTextBinding

class HolderTextMessage(view: View) : RecyclerView.ViewHolder(view) {
    private lateinit var itemBinding: MessageItemTextBinding

    val blockUserMessage: ConstraintLayout = itemBinding.blockUserMessage
    val chatUserMessage: TextView = itemBinding.chatUserMessage
    val chatUserMessageTime: TextView = itemBinding.chatUserMessageTime
    val blockReceivedMessage: ConstraintLayout = itemBinding.blockReceivedMessage
    val chatReceivedMessage: TextView = itemBinding.chatReceivedMessage
    val chatReceivedMessageTime: TextView = itemBinding.chatReceivedMessageTime
}