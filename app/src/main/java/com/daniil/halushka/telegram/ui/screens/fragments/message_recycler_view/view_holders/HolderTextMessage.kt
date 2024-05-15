package com.daniil.halushka.telegram.ui.screens.fragments.message_recycler_view.view_holders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.daniil.halushka.telegram.database.CURRENT_UID
import com.daniil.halushka.telegram.databinding.MessageItemTextBinding
import com.daniil.halushka.telegram.ui.screens.fragments.message_recycler_view.view.MessageView
import com.daniil.halushka.telegram.util.asTime

class HolderTextMessage(view: View) : RecyclerView.ViewHolder(view) {
    private val itemBinding = MessageItemTextBinding.bind(view)

    val blockUserMessage: ConstraintLayout = itemBinding.blockUserMessage
    val chatUserMessage: TextView = itemBinding.chatUserMessage
    val chatUserMessageTime: TextView = itemBinding.chatUserMessageTime
    val blockReceivedMessage: ConstraintLayout = itemBinding.blockReceivedMessage
    val chatReceivedMessage: TextView = itemBinding.chatReceivedMessage
    val chatReceivedMessageTime: TextView = itemBinding.chatReceivedMessageTime

    fun drawMessageText(
        singleChatHolder: HolderTextMessage,
        view: MessageView
    ) {

        if (view.from == CURRENT_UID) {
            singleChatHolder.blockUserMessage.visibility = View.VISIBLE
            singleChatHolder.blockReceivedMessage.visibility = View.GONE

            singleChatHolder.chatUserMessage.text = view.text
            singleChatHolder.chatUserMessageTime.text =
                view.timeStamp.asTime()
        } else {
            singleChatHolder.blockUserMessage.visibility = View.GONE
            singleChatHolder.blockReceivedMessage.visibility = View.VISIBLE

            singleChatHolder.chatReceivedMessage.text = view.text
            singleChatHolder.chatReceivedMessageTime.text =
                view.timeStamp.asTime()
        }
    }
}