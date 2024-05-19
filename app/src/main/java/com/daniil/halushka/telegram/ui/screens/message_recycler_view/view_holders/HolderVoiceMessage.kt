package com.daniil.halushka.telegram.ui.screens.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.daniil.halushka.telegram.database.CURRENT_UID
import com.daniil.halushka.telegram.databinding.MessageItemVoiceBinding
import com.daniil.halushka.telegram.ui.screens.message_recycler_view.view.MessageView
import com.daniil.halushka.telegram.util.AppVoicePlayer
import com.daniil.halushka.telegram.util.asTime

class HolderVoiceMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {
    private val itemBinding = MessageItemVoiceBinding.bind(view)

    private val moduleAppVoicePlayer = AppVoicePlayer()

    private val blockReceivedVoiceMessage: ConstraintLayout = itemBinding.blockReceivedVoiceMessage
    private val blockUserVoiceMessage: ConstraintLayout = itemBinding.blockUserVoiceMessage
    private val chatUserVoiceMessageTime: TextView = itemBinding.chatUserVoiceMessageTime
    private val chatReceivedVoiceMessageTime: TextView = itemBinding.chatReceivedVoiceMessageTime

    private val chatReceivedButtonPlay: ImageView = itemBinding.chatReceivedButtonVoicePlay
    val chatReceivedButtonStop: ImageView = itemBinding.chatReceivedButtonVoiceStop

    private val chatUserButtonPlay: ImageView = itemBinding.chatUserButtonVoicePlay
    val chatUserButtonStop: ImageView = itemBinding.chatUserButtonVoiceStop
    override fun onAttach(view: MessageView) {
        if (view.from == CURRENT_UID) {
            chatUserButtonPlay.setOnClickListener {
                chatUserButtonPlay.visibility = View.GONE
                chatUserButtonStop.visibility = View.VISIBLE
                playVoiceMessage(view){
                    chatUserButtonPlay.visibility = View.VISIBLE
                    chatUserButtonStop.visibility = View.GONE
                }
            }
        } else {
            chatReceivedButtonPlay.setOnClickListener {
                chatReceivedButtonPlay.visibility = View.GONE
                chatReceivedButtonStop.visibility = View.VISIBLE
                playVoiceMessage(view){
                    chatReceivedButtonPlay.visibility = View.VISIBLE
                    chatUserButtonStop.visibility = View.GONE
                }
            }
        }
    }

    private fun playVoiceMessage(view: MessageView, function: () -> Unit) {
        moduleAppVoicePlayer.play(view.id, view.fileUrl) {

        }
    }

    override fun onDetach() {
        chatUserButtonPlay.setOnClickListener(null)
        chatReceivedButtonPlay.setOnClickListener(null)
    }

    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blockReceivedVoiceMessage.visibility = View.GONE
            blockUserVoiceMessage.visibility = View.VISIBLE
            chatUserVoiceMessageTime.text =
                view.timeStamp.asTime()
        } else {
            blockReceivedVoiceMessage.visibility = View.VISIBLE
            blockUserVoiceMessage.visibility = View.GONE
            chatReceivedVoiceMessageTime.text =
                view.timeStamp.asTime()
        }
    }
}