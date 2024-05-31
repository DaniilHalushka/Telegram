package com.daniil.halushka.telegram.ui.screens.fragments.groups

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.data.models.CommonModel
import com.daniil.halushka.telegram.databinding.AddContactsItemBinding
import com.daniil.halushka.telegram.util.downloadAndSetImage
import de.hdodenhof.circleimageview.CircleImageView

class AddContactsAdapter : RecyclerView.Adapter<AddContactsAdapter.AddContactsHolder>() {
    private var listItems = mutableListOf<CommonModel>()

    class AddContactsHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val itemBinding = AddContactsItemBinding.bind(view)

        val itemName: TextView = itemBinding.addContactsItemName
        val itemLastMessage: TextView = itemBinding.addContactsLastMessage
        val itemPhoto: CircleImageView = itemBinding.addContactsItemPhoto
        val itemChoice: CircleImageView = itemBinding.addContactsItemChooseContact
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddContactsHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.add_contacts_item, parent, false)

        val holder = AddContactsHolder(view)
        holder.itemView.setOnClickListener {
            if (listItems[holder.absoluteAdapterPosition].isChosen) {
                holder.itemChoice.visibility = View.INVISIBLE
                listItems[holder.absoluteAdapterPosition].isChosen = false
                AddContactsFragment.listContacts.remove(listItems[holder.absoluteAdapterPosition])
            } else {
                holder.itemChoice.visibility = View.VISIBLE
                listItems[holder.absoluteAdapterPosition].isChosen = true
                AddContactsFragment.listContacts.add(listItems[holder.absoluteAdapterPosition])
            }
        }
        return holder
    }

    override fun getItemCount(): Int = listItems.size

    override fun onBindViewHolder(holder: AddContactsHolder, position: Int) {
        holder.itemName.text = listItems[position].fullname
        holder.itemLastMessage.text = listItems[position].lastMessage
        holder.itemPhoto.downloadAndSetImage(listItems[position].photoURL)
    }

    fun updateListItems(item: CommonModel) {
        listItems.add(item)
        notifyItemInserted(listItems.size)
    }
}