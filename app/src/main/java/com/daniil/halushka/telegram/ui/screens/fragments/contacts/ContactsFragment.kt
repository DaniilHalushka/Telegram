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
import com.daniil.halushka.telegram.util.APP_ACTIVITY
import com.daniil.halushka.telegram.util.CURRENT_UID
import com.daniil.halushka.telegram.util.NODE_PHONES_CONTACTS
import com.daniil.halushka.telegram.util.REF_DATABASE_ROOT
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView

class ContactsFragment : BaseFragment(R.layout.fragment_contacts) {

    private lateinit var contactsBinding: FragmentContactsBinding
    private lateinit var moduleRecyclerView: RecyclerView
    private lateinit var moduleAdapter: FirebaseRecyclerAdapter<CommonModel, ContactsHolder>
    private lateinit var moduleRefContacts: DatabaseReference
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

    private fun initializeRecyclerView() {
        moduleRecyclerView = contactsBinding.contactsRecyclerView
        moduleRefContacts = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)
    }

    class ContactsHolder(itemBinding: ContactItemBinding) : RecyclerView.ViewHolder(itemBinding.root){
        val name: TextView = itemBinding.contactFullname
        val status : TextView = itemBinding.contactStatus
        val photo : CircleImageView = itemBinding.contactPhoto
    }
}