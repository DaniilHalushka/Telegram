package com.daniil.halushka.telegram.ui.screens.fragments.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.data.models.CommonModel
import com.daniil.halushka.telegram.databinding.FragmentCreateGroupBinding
import com.daniil.halushka.telegram.ui.screens.fragments.base.BaseFragment
import com.daniil.halushka.telegram.util.APP_ACTIVITY
import com.daniil.halushka.telegram.util.getPlurals
import com.daniil.halushka.telegram.util.hideKeyboard
import com.daniil.halushka.telegram.util.showToast

class CreateGroupFragment(private var listContacts: List<CommonModel>) :
    BaseFragment(R.layout.fragment_create_group) {

    private lateinit var createGroupBinding: FragmentCreateGroupBinding

    private lateinit var moduleRecyclerView: RecyclerView
    private lateinit var moduleAdapter: AddContactsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        createGroupBinding = FragmentCreateGroupBinding.inflate(inflater, container, false)
        return createGroupBinding.root
    }

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.create_group)
        hideKeyboard()
        initializeRecyclerView()
        createGroupBinding.createGroupButtonDone.setOnClickListener {
            //TODO это для проверки
            showToast("HOORAY")
        }
        createGroupBinding.createGroupInputName.requestFocus()
        createGroupBinding.createGroupCounts.text = getPlurals(listContacts.size)
    }

    private fun initializeRecyclerView() {
        moduleRecyclerView = createGroupBinding.createGroupRecyclerView
        moduleAdapter = AddContactsAdapter()

        moduleRecyclerView.adapter = moduleAdapter

        listContacts.forEach {
            moduleAdapter.updateListItems(it)
        }
    }
}