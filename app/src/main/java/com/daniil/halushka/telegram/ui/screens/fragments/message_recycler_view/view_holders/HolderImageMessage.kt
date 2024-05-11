package com.daniil.halushka.telegram.ui.screens.fragments.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.daniil.halushka.telegram.databinding.MessageItemImageBinding

class HolderImageMessage(view: View) : RecyclerView.ViewHolder(view) {
    private lateinit var itemBinding: MessageItemImageBinding

    val blockReceivedImageMessage: ConstraintLayout = itemBinding.blockReceivedImageMessage
    val blockUserImageMessage: ConstraintLayout = itemBinding.blockUserImageMessage
    val chatUserImage: ImageView = itemBinding.chatUserImage
    val chatReceivedImage: ImageView = itemBinding.chatReceivedImage
    val chatUserImageMessageTime: TextView = itemBinding.chatUserImageMessageTime
    val chatReceivedImageMessageTime: TextView = itemBinding.chatReceivedImageMessageTime
}