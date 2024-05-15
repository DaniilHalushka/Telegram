package com.daniil.halushka.telegram.ui.screens.fragments.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.daniil.halushka.telegram.database.CURRENT_UID
import com.daniil.halushka.telegram.databinding.MessageItemImageBinding
import com.daniil.halushka.telegram.ui.screens.fragments.message_recycler_view.view.MessageView
import com.daniil.halushka.telegram.util.asTime
import com.daniil.halushka.telegram.util.downloadAndSetImage

class HolderImageMessage(view: View) : RecyclerView.ViewHolder(view) {
    private val itemBinding = MessageItemImageBinding.bind(view)

    private val blockReceivedImageMessage: ConstraintLayout = itemBinding.blockReceivedImageMessage
    private val blockUserImageMessage: ConstraintLayout = itemBinding.blockUserImageMessage
    private val chatUserImage: ImageView = itemBinding.chatUserImage
    private val chatReceivedImage: ImageView = itemBinding.chatReceivedImage
    private val chatUserImageMessageTime: TextView = itemBinding.chatUserImageMessageTime
    private val chatReceivedImageMessageTime: TextView = itemBinding.chatReceivedImageMessageTime

    fun drawMessageImage(
        singleChatHolder: HolderImageMessage,
        view: MessageView
    ) {
        if (view.from == CURRENT_UID) {
            singleChatHolder.blockReceivedImageMessage.visibility = View.GONE
            singleChatHolder.blockUserImageMessage.visibility = View.VISIBLE
            singleChatHolder.chatUserImage.downloadAndSetImage(view.fileUrl)
            singleChatHolder.chatUserImageMessageTime.text =
                view.timeStamp.asTime()
        } else {
            singleChatHolder.blockReceivedImageMessage.visibility = View.VISIBLE
            singleChatHolder.blockUserImageMessage.visibility = View.GONE
            singleChatHolder.chatReceivedImage.downloadAndSetImage(view.fileUrl)
            singleChatHolder.chatReceivedImageMessageTime.text =
                view.timeStamp.asTime()
        }
    }
}