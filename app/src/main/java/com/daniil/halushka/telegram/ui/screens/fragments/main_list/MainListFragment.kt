package com.daniil.halushka.telegram.ui.screens.fragments.main_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.data.models.CommonModel
import com.daniil.halushka.telegram.database.CURRENT_UID
import com.daniil.halushka.telegram.database.NODE_MAIN_LIST
import com.daniil.halushka.telegram.database.NODE_MESSAGES
import com.daniil.halushka.telegram.database.NODE_USERS
import com.daniil.halushka.telegram.database.REF_DATABASE_ROOT
import com.daniil.halushka.telegram.database.getCommonModel
import com.daniil.halushka.telegram.databinding.FragmentMainListBinding
import com.daniil.halushka.telegram.util.APP_ACTIVITY
import com.daniil.halushka.telegram.util.AppValueEventListener
import com.daniil.halushka.telegram.util.hideKeyboard

class MainListFragment : Fragment(R.layout.fragment_main_list) {
    private lateinit var mainListFragmentBinding: FragmentMainListBinding

    private lateinit var moduleRecyclerView: RecyclerView
    private lateinit var moduleAdapter: MainListAdapter

    private val moduleRefMainList = REF_DATABASE_ROOT.child(NODE_MAIN_LIST).child(CURRENT_UID)
    private val moduleRefUsers = REF_DATABASE_ROOT.child(NODE_USERS)
    private val moduleRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)

    private var moduleListItems = listOf<CommonModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainListFragmentBinding = FragmentMainListBinding.inflate(inflater, container, false)
        return mainListFragmentBinding.root
    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.telegram_title)
        APP_ACTIVITY.moduleAppDrawer.enableDrawer()
        hideKeyboard()
        initializeChats()
    }

    private fun initializeChats() {
        moduleRecyclerView = mainListFragmentBinding.mainListRecyclerView
        moduleAdapter = MainListAdapter()

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
}