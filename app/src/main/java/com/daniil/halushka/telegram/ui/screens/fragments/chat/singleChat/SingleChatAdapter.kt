package com.daniil.halushka.telegram.ui.screens.fragments.chat.singleChat

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.daniil.halushka.telegram.ui.screens.messages.view.MessageView
import com.daniil.halushka.telegram.ui.screens.messages.holders.AppHolderFactory
import com.daniil.halushka.telegram.ui.screens.messages.holders.MessageHolder

class SingleChatAdapter : RecyclerView.Adapter<ViewHolder>() {
    private var moduleListMessagesCache = mutableListOf<MessageView>()
    private var moduleListHolders = mutableListOf<MessageHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return AppHolderFactory.getHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return moduleListMessagesCache[position].getTypeView()
    }

    override fun getItemCount(): Int = moduleListMessagesCache.size

    override fun onBindViewHolder(singleChatHolder: ViewHolder, position: Int) {
        (singleChatHolder as MessageHolder).drawMessage(moduleListMessagesCache[position])
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        (holder as MessageHolder).onAttach(moduleListMessagesCache[holder.absoluteAdapterPosition])
        moduleListHolders.add((holder as MessageHolder))
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        (holder as MessageHolder).onDetach()
        moduleListHolders.remove((holder as MessageHolder))
        super.onViewDetachedFromWindow(holder)
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

    fun onDestroy() {
        moduleListHolders.forEach { element ->
            element.onDetach()
        }
    }
}
