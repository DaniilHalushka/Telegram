package com.daniil.halushka.telegram.ui.screens.fragments.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.daniil.halushka.telegram.database.CURRENT_UID
import com.daniil.halushka.telegram.databinding.MessageItemVoiceBinding
import com.daniil.halushka.telegram.ui.screens.fragments.message_recycler_view.view.MessageView
import com.daniil.halushka.telegram.util.asTime

class HolderVoiceMessage(view: View) : RecyclerView.ViewHolder(view) {
    private lateinit var itemBinding: MessageItemVoiceBinding

    private val blockReceivedVoiceMessage: ConstraintLayout = itemBinding.blockReceivedVoiceMessage
    private val blockUserVoiceMessage: ConstraintLayout = itemBinding.blockUserVoiceMessage
    private val chatUserVoiceMessageTime: TextView = itemBinding.chatUserVoiceMessageTime
    private val chatReceivedVoiceMessageTime: TextView = itemBinding.chatReceivedVoiceMessageTime

    val chatReceivedButtonPlay: ImageView = itemBinding.chatReceivedButtonVoicePlay
    val chatReceivedButtonStop: ImageView = itemBinding.chatReceivedButtonVoiceStop

    val chatUserButtonPlay: ImageView = itemBinding.chatUserButtonVoicePlay
    val chatUserButtonStop: ImageView = itemBinding.chatUserButtonVoiceStop


    fun drawMessageVoice(
        singleChatHolder: HolderVoiceMessage,
        view: MessageView
    ) {
        if (view.from == CURRENT_UID) {
            singleChatHolder.blockReceivedVoiceMessage.visibility = View.GONE
            singleChatHolder.blockUserVoiceMessage.visibility = View.VISIBLE
            singleChatHolder.chatUserVoiceMessageTime.text =
                view.timeStamp.asTime()
        } else {
            singleChatHolder.blockReceivedVoiceMessage.visibility = View.VISIBLE
            singleChatHolder.blockUserVoiceMessage.visibility = View.GONE
            singleChatHolder.chatReceivedVoiceMessageTime.text =
                view.timeStamp.asTime()
        }
    }
}