package com.daniil.halushka.telegram.ui.screens.fragments.chat.single_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.daniil.halushka.telegram.data.models.CommonModel
import com.daniil.halushka.telegram.databinding.MessageItemBinding
import com.daniil.halushka.telegram.database.CURRENT_UID
import com.daniil.halushka.telegram.util.asTime

class SingleChatAdapter : RecyclerView.Adapter<SingleChatAdapter.SingleChatHolder>() {
    private var moduleListMessagesCache = emptyList<CommonModel>()

    class SingleChatHolder(itemBinding: MessageItemBinding) : ViewHolder(itemBinding.root) {


        val blockUserMessage: ConstraintLayout = itemBinding.blockUserMessage
        val chatUserMessage: TextView = itemBinding.chatUserMessage
        val chatUserMessageTime: TextView = itemBinding.chatUserMessageTime

        val blockReceivedMessage: ConstraintLayout = itemBinding.blockReceivedMessage
        val chatReceivedMessage: TextView = itemBinding.chatReceivedMessage
        val chatReceivedMessageTime: TextView = itemBinding.chatReceivedMessageTime
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatHolder {
        val messageItemBinding = MessageItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return SingleChatHolder(messageItemBinding)
    }

    override fun getItemCount(): Int = moduleListMessagesCache.size

    override fun onBindViewHolder(singleChatHolder: SingleChatHolder, position: Int) {
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

    fun setList(list: List<CommonModel>) {
        moduleListMessagesCache = list
        notifyDataSetChanged()                  //TODO поменять потом, когда будет использоваться DiffUtil
    }
}
