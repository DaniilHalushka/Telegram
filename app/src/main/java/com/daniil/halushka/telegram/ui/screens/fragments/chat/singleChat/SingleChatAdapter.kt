package com.daniil.halushka.telegram.ui.screens.fragments.chat.singleChat

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.daniil.halushka.telegram.ui.screens.fragments.message_recycler_view.view.MessageView
import com.daniil.halushka.telegram.ui.screens.fragments.message_recycler_view.view_holders.AppHolderFactory
import com.daniil.halushka.telegram.ui.screens.fragments.message_recycler_view.view_holders.HolderImageMessage
import com.daniil.halushka.telegram.ui.screens.fragments.message_recycler_view.view_holders.HolderTextMessage
import com.daniil.halushka.telegram.ui.screens.fragments.message_recycler_view.view_holders.HolderVoiceMessage

class SingleChatAdapter : RecyclerView.Adapter<ViewHolder>() {
    private var moduleListMessagesCache = mutableListOf<MessageView>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return AppHolderFactory.getHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return moduleListMessagesCache[position].getTypeView()
    }

    override fun getItemCount(): Int = moduleListMessagesCache.size

    override fun onBindViewHolder(singleChatHolder: ViewHolder, position: Int) {
        when (singleChatHolder) {
            is HolderImageMessage -> singleChatHolder.drawMessageImage(
                singleChatHolder,
                moduleListMessagesCache[position]
            )

            is HolderTextMessage -> singleChatHolder.drawMessageText(
                singleChatHolder,
                moduleListMessagesCache[position]
            )

            is HolderVoiceMessage -> singleChatHolder.drawMessageVoice(
                singleChatHolder,
                moduleListMessagesCache[position]
            )

            else -> {}
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
