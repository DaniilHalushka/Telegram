package com.daniil.halushka.telegram.ui.screens.fragments.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.data.models.CommonModel
import com.daniil.halushka.telegram.database.CURRENT_UID
import com.daniil.halushka.telegram.database.NODE_PHONES_CONTACTS
import com.daniil.halushka.telegram.database.NODE_USERS
import com.daniil.halushka.telegram.database.REF_DATABASE_ROOT
import com.daniil.halushka.telegram.database.getCommonModel
import com.daniil.halushka.telegram.databinding.ContactItemBinding
import com.daniil.halushka.telegram.databinding.FragmentContactsBinding
import com.daniil.halushka.telegram.ui.screens.fragments.base.BaseFragment
import com.daniil.halushka.telegram.ui.screens.fragments.chat.singleChat.SingleChatFragment
import com.daniil.halushka.telegram.util.APP_ACTIVITY
import com.daniil.halushka.telegram.util.AppValueEventListener
import com.daniil.halushka.telegram.util.downloadAndSetImage
import com.daniil.halushka.telegram.util.replaceFragment
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView

class ContactsFragment : BaseFragment(R.layout.fragment_contacts) {

    private lateinit var binding: FragmentContactsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FirebaseRecyclerAdapter<CommonModel, ContactsHolder>
    private lateinit var contactsRef: DatabaseReference
    private val listenersMap = hashMapOf<DatabaseReference, AppValueEventListener>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.contacts_title)
        initializeRecyclerView()
    }

    override fun onPause() {
        super.onPause()
        cleanupListeners()
    }

    private fun initializeRecyclerView() {
        recyclerView = binding.contactsRecyclerView
        contactsRef = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)

        val options = FirebaseRecyclerOptions.Builder<CommonModel>()
            .setQuery(contactsRef, CommonModel::class.java)
            .build()

        adapter = object : FirebaseRecyclerAdapter<CommonModel, ContactsHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
                val itemBinding = ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ContactsHolder(itemBinding)
            }

            override fun onBindViewHolder(holder: ContactsHolder, position: Int, model: CommonModel) {
                bindContact(holder, model)
            }
        }

        recyclerView.adapter = adapter
        adapter.startListening()
    }

    private fun bindContact(holder: ContactsHolder, commonModel: CommonModel) {
        val userRef = REF_DATABASE_ROOT.child(NODE_USERS).child(commonModel.id)
        val userListener = AppValueEventListener { snapshot ->
            val contact = snapshot.getCommonModel()

            holder.name.text = contact.fullname.ifEmpty { commonModel.fullname }
            holder.status.text = contact.state
            holder.photo.downloadAndSetImage(contact.photoURL)
            holder.itemView.setOnClickListener {
                replaceFragment(SingleChatFragment(commonModel), R.id.main_data_container)
            }
        }

        userRef.addValueEventListener(userListener)
        listenersMap[userRef] = userListener
    }

    private fun cleanupListeners() {
        adapter.stopListening()
        listenersMap.forEach { (ref, listener) ->
            ref.removeEventListener(listener)
        }
        listenersMap.clear()
    }

    class ContactsHolder(binding: ContactItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.contactFullname
        val status: TextView = binding.contactStatus
        val photo: CircleImageView = binding.contactPhoto
    }
}
