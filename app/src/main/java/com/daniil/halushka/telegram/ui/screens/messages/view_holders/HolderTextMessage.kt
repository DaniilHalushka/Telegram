package com.daniil.halushka.telegram.ui.screens.messages.view_holders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.daniil.halushka.telegram.database.CURRENT_UID
import com.daniil.halushka.telegram.databinding.MessageItemTextBinding
import com.daniil.halushka.telegram.ui.screens.messages.view.MessageView
import com.daniil.halushka.telegram.util.asTime

class HolderTextMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {
    private val itemBinding = MessageItemTextBinding.bind(view)

    private val blockUserMessage: ConstraintLayout = itemBinding.blockUserMessage
    private val chatUserMessage: TextView = itemBinding.chatUserMessage
    private val chatUserMessageTime: TextView = itemBinding.chatUserMessageTime
    private val blockReceivedMessage: ConstraintLayout = itemBinding.blockReceivedMessage
    private val chatReceivedMessage: TextView = itemBinding.chatReceivedMessage
    private val chatReceivedMessageTime: TextView = itemBinding.chatReceivedMessageTime
    override fun onAttach(view: MessageView) {}

    override fun onDetach() {}

    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blockUserMessage.visibility = View.VISIBLE
            blockReceivedMessage.visibility = View.GONE

            chatUserMessage.text = view.text
            chatUserMessageTime.text =
                view.timeStamp.asTime()
        } else {
            blockUserMessage.visibility = View.GONE
            blockReceivedMessage.visibility = View.VISIBLE

            chatReceivedMessage.text = view.text
            chatReceivedMessageTime.text =
                view.timeStamp.asTime()
        }
    }
}