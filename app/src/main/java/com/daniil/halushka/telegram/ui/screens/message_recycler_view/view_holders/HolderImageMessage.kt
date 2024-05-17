package com.daniil.halushka.telegram.ui.screens.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.daniil.halushka.telegram.database.CURRENT_UID
import com.daniil.halushka.telegram.databinding.MessageItemImageBinding
import com.daniil.halushka.telegram.ui.screens.message_recycler_view.view.MessageView
import com.daniil.halushka.telegram.util.asTime
import com.daniil.halushka.telegram.util.downloadAndSetImage

class HolderImageMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {
    private val itemBinding = MessageItemImageBinding.bind(view)

    private val blockReceivedImageMessage: ConstraintLayout = itemBinding.blockReceivedImageMessage
    private val blockUserImageMessage: ConstraintLayout = itemBinding.blockUserImageMessage
    private val chatUserImage: ImageView = itemBinding.chatUserImage
    private val chatReceivedImage: ImageView = itemBinding.chatReceivedImage
    private val chatUserImageMessageTime: TextView = itemBinding.chatUserImageMessageTime
    private val chatReceivedImageMessageTime: TextView = itemBinding.chatReceivedImageMessageTime
    override fun onAttach(view: MessageView) {
        TODO("Not yet implemented")
    }

    override fun onDetach() {
        TODO("Not yet implemented")
    }

    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blockReceivedImageMessage.visibility = View.GONE
            blockUserImageMessage.visibility = View.VISIBLE
            chatUserImage.downloadAndSetImage(view.fileUrl)
            chatUserImageMessageTime.text =
                view.timeStamp.asTime()
        } else {
            blockReceivedImageMessage.visibility = View.VISIBLE
            blockUserImageMessage.visibility = View.GONE
            chatReceivedImage.downloadAndSetImage(view.fileUrl)
            chatReceivedImageMessageTime.text =
                view.timeStamp.asTime()
        }
    }
}