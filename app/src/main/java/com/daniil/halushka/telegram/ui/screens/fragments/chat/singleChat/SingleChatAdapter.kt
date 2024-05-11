package com.daniil.halushka.telegram.ui.screens.fragments.chat.singleChat

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.daniil.halushka.telegram.database.CURRENT_UID
import com.daniil.halushka.telegram.ui.screens.fragments.message_recycler_view.view.MessageView
import com.daniil.halushka.telegram.ui.screens.fragments.message_recycler_view.view_holders.AppHolderFactory
import com.daniil.halushka.telegram.ui.screens.fragments.message_recycler_view.view_holders.HolderImageMessage
import com.daniil.halushka.telegram.ui.screens.fragments.message_recycler_view.view_holders.HolderTextMessage
import com.daniil.halushka.telegram.util.asTime
import com.daniil.halushka.telegram.util.downloadAndSetImage

class SingleChatAdapter : RecyclerView.Adapter<ViewHolder>() {
    private var moduleListMessagesCache = mutableListOf<MessageView>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return AppHolderFactory.getHolder(parent, viewType)
    }

    override fun getItemCount(): Int = moduleListMessagesCache.size

    override fun onBindViewHolder(singleChatHolder: ViewHolder, position: Int) {
        when (singleChatHolder) {
            is HolderImageMessage -> drawMessageImage(singleChatHolder, position)
            is HolderTextMessage -> drawMessageText(singleChatHolder, position)
            else -> {}
        }

    }

    private fun drawMessageImage(
        singleChatHolder: HolderImageMessage,
        position: Int
    ) {
        if (moduleListMessagesCache[position].from == CURRENT_UID) {
            singleChatHolder.blockReceivedImageMessage.visibility = View.GONE
            singleChatHolder.blockUserImageMessage.visibility = View.VISIBLE
            singleChatHolder.chatUserImage.downloadAndSetImage(moduleListMessagesCache[position].fileUrl)
            singleChatHolder.chatUserImageMessageTime.text =
                moduleListMessagesCache[position].timeStamp.asTime()
        } else {
            singleChatHolder.blockReceivedImageMessage.visibility = View.VISIBLE
            singleChatHolder.blockUserImageMessage.visibility = View.GONE
            singleChatHolder.chatReceivedImage.downloadAndSetImage(moduleListMessagesCache[position].fileUrl)
            singleChatHolder.chatReceivedImageMessageTime.text =
                moduleListMessagesCache[position].timeStamp.asTime()
        }
    }

    private fun drawMessageText(
        singleChatHolder: HolderTextMessage,
        position: Int
    ) {

        if (moduleListMessagesCache[position].from == CURRENT_UID) {
            singleChatHolder.blockUserMessage.visibility = View.VISIBLE
            singleChatHolder.blockReceivedMessage.visibility = View.GONE

            singleChatHolder.chatUserMessage.text = moduleListMessagesCache[position].text
            singleChatHolder.chatUserMessageTime.text =
                moduleListMessagesCache[position].timeStamp.asTime()
        } else {
            singleChatHolder.blockUserMessage.visibility = View.GONE
            singleChatHolder.blockReceivedMessage.visibility = View.VISIBLE

            singleChatHolder.chatReceivedMessage.text = moduleListMessagesCache[position].text
            singleChatHolder.chatReceivedMessageTime.text =
                moduleListMessagesCache[position].timeStamp.asTime()
        }
    }

    fun addItemToBottom(
        item: MessageView,
        onSuccess: () -> Unit
    ) {
        if (!moduleListMessagesCache.contains(item)) {
            moduleListMessagesCache.add(item)
            notifyItemInserted(moduleListMessagesCache.size)
        }
        onSuccess()
    }

    fun addItemToTop(
        item: MessageView,
        onSuccess: () -> Unit
    ) {
        if (!moduleListMessagesCache.contains(item)) {
            moduleListMessagesCache.add(item)
            moduleListMessagesCache.sortBy { it.timeStamp }
            notifyItemInserted(0)
        }
        onSuccess()
    }
}
