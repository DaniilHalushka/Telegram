package com.daniil.halushka.telegram.ui.screens.messages.holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.daniil.halushka.telegram.database.CURRENT_UID
import com.daniil.halushka.telegram.databinding.MessageItemImageBinding
import com.daniil.halushka.telegram.ui.screens.messages.view.MessageView
import com.daniil.halushka.telegram.util.asTime
import com.daniil.halushka.telegram.util.downloadAndSetImage

class HolderImageMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {
    private val binding = MessageItemImageBinding.bind(view)
    private val userMessageBlock: ConstraintLayout = binding.blockUserImageMessage
    private val receivedMessageBlock: ConstraintLayout = binding.blockReceivedImageMessage
    private val userImage: ImageView = binding.chatUserImage
    private val receivedImage: ImageView = binding.chatReceivedImage
    private val userMessageTime: TextView = binding.chatUserImageMessageTime
    private val receivedMessageTime: TextView = binding.chatReceivedImageMessageTime

    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            displayUserMessage(view)
        } else {
            displayReceivedMessage(view)
        }
    }

    private fun displayUserMessage(view: MessageView) {
        receivedMessageBlock.visibility = View.GONE
        userMessageBlock.visibility = View.VISIBLE
        userImage.downloadAndSetImage(view.fileUrl)
        userMessageTime.text = view.timeStamp.asTime()
    }

    private fun displayReceivedMessage(view: MessageView) {
        receivedMessageBlock.visibility = View.VISIBLE
        userMessageBlock.visibility = View.GONE
        receivedImage.downloadAndSetImage(view.fileUrl)
        receivedMessageTime.text = view.timeStamp.asTime()
    }

    override fun onAttach(view: MessageView) {}

    override fun onDetach() {}
}
