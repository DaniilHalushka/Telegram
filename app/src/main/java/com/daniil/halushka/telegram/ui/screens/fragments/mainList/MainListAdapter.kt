package com.daniil.halushka.telegram.ui.screens.fragments.mainList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.data.models.CommonModel
import com.daniil.halushka.telegram.databinding.MainListItemBinding
import com.daniil.halushka.telegram.ui.screens.fragments.chat.singleChat.SingleChatFragment
import com.daniil.halushka.telegram.ui.screens.fragments.groups.GroupChatFragment
import com.daniil.halushka.telegram.util.TYPE_CHAT
import com.daniil.halushka.telegram.util.TYPE_GROUP
import com.daniil.halushka.telegram.util.downloadAndSetImage
import com.daniil.halushka.telegram.util.replaceFragment
import de.hdodenhof.circleimageview.CircleImageView

class MainListAdapter : RecyclerView.Adapter<MainListAdapter.MainListHolder>() {
    private var listItems = mutableListOf<CommonModel>()

    class MainListHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val itemBinding = MainListItemBinding.bind(view)

        val itemName: TextView = itemBinding.mainListItemName
        val itemLastMessage: TextView = itemBinding.mainListLastMessage
        val itemPhoto: CircleImageView = itemBinding.mainListItemPhoto
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.main_list_item, parent, false)

        val holder = MainListHolder(view)
        holder.itemView.setOnClickListener {
            when (listItems[holder.absoluteAdapterPosition].type) {
                TYPE_CHAT -> replaceFragment(
                    SingleChatFragment(listItems[holder.absoluteAdapterPosition]),
                    R.id.main_data_container
                )

                TYPE_GROUP -> replaceFragment(
                    GroupChatFragment(listItems[holder.absoluteAdapterPosition]),
                    R.id.main_data_container
                )
            }
        }
        return holder
    }

    override fun getItemCount(): Int = listItems.size

    override fun onBindViewHolder(holder: MainListHolder, position: Int) {
        holder.itemName.text = listItems[position].fullname
        holder.itemLastMessage.text = listItems[position].lastMessage
        holder.itemPhoto.downloadAndSetImage(listItems[position].photoURL)
    }

    fun updateListItems(item: CommonModel) {
        listItems.add(item)
        notifyItemInserted(listItems.size)
    }
}