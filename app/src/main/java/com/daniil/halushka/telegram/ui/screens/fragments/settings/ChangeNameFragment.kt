package com.daniil.halushka.telegram.ui.screens.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.databinding.FragmentChangeNameBinding
import com.daniil.halushka.telegram.ui.screens.fragments.BaseChangeFragment
import com.daniil.halushka.telegram.util.CHILD_FULLNAME
import com.daniil.halushka.telegram.util.NODE_USERS
import com.daniil.halushka.telegram.util.REF_DATABASE_ROOT
import com.daniil.halushka.telegram.util.UID
import com.daniil.halushka.telegram.util.USER
import com.daniil.halushka.telegram.util.showToast

class ChangeNameFragment : BaseChangeFragment(R.layout.fragment_change_name) {
    private lateinit var changeNameBinding: FragmentChangeNameBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        changeNameBinding = FragmentChangeNameBinding.inflate(inflater, container, false)
        return changeNameBinding.root
    }


    override fun onResume() {
        super.onResume()
        initializeFullnameList()
    }

    private fun initializeFullnameList() {
        val fullNameList = USER.fullname.split(" ")
        if (fullNameList.size > 1) {
            changeNameBinding.settingsInputName.setText(fullNameList[0])
            changeNameBinding.settingsInputSurname.setText(fullNameList[1])
        } else {
            changeNameBinding.settingsInputName.setText("")
        }
    }

    override fun change() {
        val name = changeNameBinding.settingsInputName.text.toString()
        val surname = changeNameBinding.settingsInputSurname.text.toString()

        if (name.isEmpty()) {
            showToast(getString(R.string.settings_toast_empty_name))
        } else {
            val fullName = "$name $surname"
            REF_DATABASE_ROOT.child(NODE_USERS)
                .child(UID)
                .child(CHILD_FULLNAME)
                .setValue(fullName).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast(getString(R.string.toast_details_update))
                        USER.fullname = fullName
                        parentFragmentManager.popBackStack()
                    }
                }
        }
    }
}