package com.daniil.halushka.telegram.ui.screens.fragments.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.data.models.CommonModel
import com.daniil.halushka.telegram.database.CURRENT_UID
import com.daniil.halushka.telegram.database.NODE_MAIN_LIST
import com.daniil.halushka.telegram.database.NODE_MESSAGES
import com.daniil.halushka.telegram.database.NODE_USERS
import com.daniil.halushka.telegram.database.REF_DATABASE_ROOT
import com.daniil.halushka.telegram.database.getCommonModel
import com.daniil.halushka.telegram.databinding.FragmentAddContactsBinding
import com.daniil.halushka.telegram.ui.screens.fragments.base.BaseFragment
import com.daniil.halushka.telegram.util.APP_ACTIVITY
import com.daniil.halushka.telegram.util.AppValueEventListener
import com.daniil.halushka.telegram.util.hideKeyboard
import com.daniil.halushka.telegram.util.replaceFragment
import com.daniil.halushka.telegram.util.showToast

class AddContactsFragment : BaseFragment(R.layout.fragment_add_contacts) {
    private lateinit var addContactsFragmentBinding: FragmentAddContactsBinding

    private lateinit var moduleRecyclerView: RecyclerView
    private lateinit var moduleAdapter: AddContactsAdapter

    private val moduleRefMainList = REF_DATABASE_ROOT.child(NODE_MAIN_LIST).child(CURRENT_UID)
    private val moduleRefUsers = REF_DATABASE_ROOT.child(NODE_USERS)
    private val moduleRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)

    private var moduleListItems = listOf<CommonModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addContactsFragmentBinding = FragmentAddContactsBinding.inflate(inflater, container, false)
        return addContactsFragmentBinding.root
    }

    override fun onResume() {
        listContacts.clear()
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.add_people_to_chat)
        hideKeyboard()
        initializeRecyclerView()
        addContactsFragmentBinding.addContactsNextButton.setOnClickListener {
            if (listContacts.isEmpty()) showToast(getString(R.string.add_members_to_chat))
            else replaceFragment(CreateGroupFragment(listContacts), R.id.main_data_container)
        }
    }

    private fun initializeRecyclerView() {
        moduleRecyclerView = addContactsFragmentBinding.addContactsRecyclerView
        moduleAdapter = AddContactsAdapter()

        moduleRefMainList.addListenerForSingleValueEvent(AppValueEventListener { items ->

            moduleListItems = items.children.map { child ->
                child.getCommonModel()
            }

            moduleListItems.forEach { model ->

                moduleRefUsers.child(model.id)
                    .addListenerForSingleValueEvent(AppValueEventListener { user ->

                        val newModel = user.getCommonModel()
                        moduleRefMessages.child(model.id).limitToLast(1)
                            .addListenerForSingleValueEvent(AppValueEventListener { message ->
                                val helpList = message.children.map { it.getCommonModel() }

                                if (helpList.isEmpty()) {
                                    newModel.lastMessage = getString(R.string.clear_chat)
                                } else {
                                    newModel.lastMessage = helpList[0].text
                                }

                                if (newModel.fullname.isEmpty()) {
                                    newModel.fullname = newModel.phone
                                }
                                moduleAdapter.updateListItems(newModel)
                            })
                    })
            }
        })

        moduleRecyclerView.adapter = moduleAdapter
    }

    companion object {
        val listContacts = mutableListOf<CommonModel>()
    }
}