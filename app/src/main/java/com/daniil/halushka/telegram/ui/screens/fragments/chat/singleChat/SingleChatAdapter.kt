package com.daniil.halushka.telegram.ui.screens.fragments.chat.singleChat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.daniil.halushka.telegram.data.models.CommonModel
import com.daniil.halushka.telegram.database.CURRENT_UID
import com.daniil.halushka.telegram.databinding.MessageItemBinding
import com.daniil.halushka.telegram.util.TYPE_MESSAGE_IMAGE
import com.daniil.halushka.telegram.util.TYPE_MESSAGE_TEXT
import com.daniil.halushka.telegram.util.asTime
import com.daniil.halushka.telegram.util.downloadAndSetImage

class SingleChatAdapter : RecyclerView.Adapter<SingleChatAdapter.SingleChatHolder>() {
    private var moduleListMessagesCache = mutableListOf<CommonModel>()

    class SingleChatHolder(itemBinding: MessageItemBinding) : ViewHolder(itemBinding.root) {
        //Text
        val blockUserMessage: ConstraintLayout = itemBinding.blockUserMessage
        val chatUserMessage: TextView = itemBinding.chatUserMessage
        val chatUserMessageTime: TextView = itemBinding.chatUserMessageTime
        val blockReceivedMessage: ConstraintLayout = itemBinding.blockReceivedMessage
        val chatReceivedMessage: TextView = itemBinding.chatReceivedMessage
        val chatReceivedMessageTime: TextView = itemBinding.chatReceivedMessageTime

        //Image
        val blockReceivedImageMessage: ConstraintLayout = itemBinding.blockReceivedImageMessage
        val blockUserImageMessage: ConstraintLayout = itemBinding.blockUserImageMessage
        val chatUserImage: ImageView = itemBinding.chatUserImage
        val chatReceivedImage: ImageView = itemBinding.chatReceivedImage
        val chatUserImageMessageTime: TextView = itemBinding.chatUserImageMessageTime
        val chatReceivedImageMessageTime: TextView = itemBinding.chatReceivedImageMessageTime

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatHolder {
        val messageItemBinding = MessageItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return SingleChatHolder(messageItemBinding)
    }

    override fun getItemCount(): Int = moduleListMessagesCache.size

    override fun onBindViewHolder(singleChatHolder: SingleChatHolder, position: Int) {
        when (moduleListMessagesCache[position].type) {
            TYPE_MESSAGE_TEXT -> drawMessageText(singleChatHolder, position)
            TYPE_MESSAGE_IMAGE -> drawMessageImage(singleChatHolder, position)
        }

    }

    private fun drawMessageImage(
        singleChatHolder: SingleChatHolder,
        position: Int
    ) {
        singleChatHolder.blockUserMessage.visibility = View.GONE
        singleChatHolder.blockReceivedMessage.visibility = View.GONE

        if (moduleListMessagesCache[position].from == CURRENT_UID) {
            singleChatHolder.blockReceivedImageMessage.visibility = View.GONE
            singleChatHolder.blockUserImageMessage.visibility = View.VISIBLE
            singleChatHolder.chatUserImage.downloadAndSetImage(moduleListMessagesCache[position].imageUrl)
            singleChatHolder.chatUserImageMessageTime.text =
                moduleListMessagesCache[position].timeStamp.toString().asTime()
        } else {
            singleChatHolder.blockReceivedImageMessage.visibility = View.VISIBLE
            singleChatHolder.blockUserImageMessage.visibility = View.GONE
            singleChatHolder.chatReceivedImage.downloadAndSetImage(moduleListMessagesCache[position].imageUrl)
            singleChatHolder.chatReceivedImageMessageTime.text =
                moduleListMessagesCache[position].timeStamp.toString().asTime()
        }
    }

    private fun drawMessageText(
        singleChatHolder: SingleChatHolder,
        position: Int
    ) {
        singleChatHolder.blockUserImageMessage.visibility = View.GONE
        singleChatHolder.blockReceivedImageMessage.visibility = View.GONE

        if (moduleListMessagesCache[position].from == CURRENT_UID) {
            singleChatHolder.blockUserMessage.visibility = View.VISIBLE
            singleChatHolder.blockReceivedMessage.visibility = View.GONE

            singleChatHolder.chatUserMessage.text = moduleListMessagesCache[position].text
            singleChatHolder.chatUserMessageTime.text =
                moduleListMessagesCache[position].timeStamp.toString().asTime()
        } else {
            singleChatHolder.blockUserMessage.visibility = View.GONE
            singleChatHolder.blockReceivedMessage.visibility = View.VISIBLE

            singleChatHolder.chatReceivedMessage.text = moduleListMessagesCache[position].text
            singleChatHolder.chatReceivedMessageTime.text =
                moduleListMessagesCache[position].timeStamp.toString().asTime()
        }
    }

    fun addItemToBottom(
        item: CommonModel,
        onSuccess: () -> Unit
    ) {
        if (!moduleListMessagesCache.contains(item)) {
            moduleListMessagesCache.add(item)
            notifyItemInserted(moduleListMessagesCache.size)
        }
        onSuccess()
    }

    fun addItemToTop(
        item: CommonModel,
        onSuccess: () -> Unit
    ) {
        if (!moduleListMessagesCache.contains(item)) {
            moduleListMessagesCache.add(item)
            moduleListMessagesCache.sortBy { it.timeStamp.toString() }
            notifyItemInserted(0)
        }
        onSuccess()
    }
}
