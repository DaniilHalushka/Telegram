package com.daniil.halushka.telegram.ui.screens.fragments.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.data.models.CommonModel
import com.daniil.halushka.telegram.databinding.ContactItemBinding
import com.daniil.halushka.telegram.databinding.FragmentContactsBinding
import com.daniil.halushka.telegram.ui.screens.fragments.BaseFragment
import com.daniil.halushka.telegram.ui.screens.fragments.chat.SingleChatFragment
import com.daniil.halushka.telegram.util.APP_ACTIVITY
import com.daniil.halushka.telegram.util.AppValueEventListener
import com.daniil.halushka.telegram.util.CURRENT_UID
import com.daniil.halushka.telegram.util.NODE_PHONES_CONTACTS
import com.daniil.halushka.telegram.util.NODE_USERS
import com.daniil.halushka.telegram.util.REF_DATABASE_ROOT
import com.daniil.halushka.telegram.util.downloadAndSetImage
import com.daniil.halushka.telegram.util.getCommonModel
import com.daniil.halushka.telegram.util.replaceParentFragment
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView

class ContactsFragment : BaseFragment(R.layout.fragment_contacts) {

    private lateinit var contactsBinding: FragmentContactsBinding
    private lateinit var moduleRecyclerView: RecyclerView
    private lateinit var moduleAdapter: FirebaseRecyclerAdapter<CommonModel, ContactsHolder>
    private lateinit var moduleRefContacts: DatabaseReference
    private lateinit var moduleRefUsersListener: AppValueEventListener
    private lateinit var moduleRefUsers: DatabaseReference
    private var mapListeners = hashMapOf<DatabaseReference, AppValueEventListener>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        contactsBinding = FragmentContactsBinding.inflate(inflater, container, false)
        return contactsBinding.root
    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.contacts_title)
        initializeRecyclerView()

    }

    override fun onPause() {
        super.onPause()
        moduleAdapter.stopListening()
        mapListeners.forEach {
            it.key.removeEventListener(it.value)
        }
    }

    private fun initializeRecyclerView() {
        moduleRecyclerView = contactsBinding.contactsRecyclerView
        moduleRefContacts = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)

        val options = FirebaseRecyclerOptions.Builder<CommonModel>()
            .setQuery(moduleRefContacts, CommonModel::class.java)
            .build()

        moduleAdapter = object : FirebaseRecyclerAdapter<CommonModel, ContactsHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
                val contactItemBinding = ContactItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)

                return ContactsHolder(contactItemBinding)
            }

            override fun onBindViewHolder(
                contactsHolder: ContactsHolder,
                position: Int,
                commonModel: CommonModel
            ) {
                moduleRefUsers = REF_DATABASE_ROOT.child(NODE_USERS)
                    .child(commonModel.id)
                moduleRefUsersListener = AppValueEventListener {
                    val contact = it.getCommonModel()

                    contactsHolder.name.text = contact.fullname
                    contactsHolder.status.text = contact.state
                    contactsHolder.photo.downloadAndSetImage(contact.photoURL)
                    contactsHolder.itemView.setOnClickListener {
                        replaceParentFragment(
                            SingleChatFragment(contact),
                            R.id.main_data_container
                        )
                    }
                }
                moduleRefUsers.addValueEventListener(moduleRefUsersListener)
                mapListeners[moduleRefUsers] = moduleRefUsersListener
            }
        }

        moduleRecyclerView.adapter = moduleAdapter
        moduleAdapter.startListening()
    }

    class ContactsHolder(itemBinding: ContactItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val name: TextView = itemBinding.contactFullname
        val status: TextView = itemBinding.contactStatus
        val photo: CircleImageView = itemBinding.contactPhoto
    }
}

