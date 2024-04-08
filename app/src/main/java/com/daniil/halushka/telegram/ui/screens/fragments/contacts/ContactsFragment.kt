package com.daniil.halushka.telegram.ui.screens.fragments.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.databinding.FragmentContactsBinding
import com.daniil.halushka.telegram.ui.screens.fragments.BaseFragment
import com.daniil.halushka.telegram.util.APP_ACTIVITY

class ContactsFragment : BaseFragment(R.layout.fragment_contacts) {

    private lateinit var contactsBinding: FragmentContactsBinding
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
    }
}