package com.daniil.halushka.telegram.ui.screens.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.databinding.FragmentChangeInformationBinding
import com.daniil.halushka.telegram.ui.screens.fragments.BaseChangeFragment
import com.daniil.halushka.telegram.database.CHILD_INFORMATION
import com.daniil.halushka.telegram.database.CURRENT_UID
import com.daniil.halushka.telegram.database.NODE_USERS
import com.daniil.halushka.telegram.database.REF_DATABASE_ROOT
import com.daniil.halushka.telegram.database.USER
import com.daniil.halushka.telegram.util.showFragmentToast

class ChangeInformationFragment : BaseChangeFragment(R.layout.fragment_change_information) {
    private lateinit var changeInformationFragment: FragmentChangeInformationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        changeInformationFragment =
            FragmentChangeInformationBinding.inflate(inflater, container, false)
        return changeInformationFragment.root
    }

    override fun onResume() {
        super.onResume()
        changeInformationFragment.settingsInputInformation.setText(USER.information)
    }

    override fun change() {
        super.change()
        val newInformation = changeInformationFragment.settingsInputInformation.text.toString()
        REF_DATABASE_ROOT.child(NODE_USERS)
            .child(CURRENT_UID)
            .child(CHILD_INFORMATION)
            .setValue(newInformation)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showFragmentToast(getString(R.string.toast_details_update))
                    USER.information = newInformation
                    parentFragmentManager.popBackStack()
                }

            }
    }
}