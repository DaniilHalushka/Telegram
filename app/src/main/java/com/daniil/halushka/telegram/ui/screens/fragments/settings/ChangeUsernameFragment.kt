package com.daniil.halushka.telegram.ui.screens.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.databinding.FragmentChangeUsernameBinding
import com.daniil.halushka.telegram.ui.screens.fragments.BaseChangeFragment
import com.daniil.halushka.telegram.util.AppValueEventListener
import com.daniil.halushka.telegram.util.CHILD_USERNAME
import com.daniil.halushka.telegram.util.NODE_USERNAMES
import com.daniil.halushka.telegram.util.NODE_USERS
import com.daniil.halushka.telegram.util.REF_DATABASE_ROOT
import com.daniil.halushka.telegram.util.CURRENT_UID
import com.daniil.halushka.telegram.util.USER
import com.daniil.halushka.telegram.util.showFragmentToast
import java.util.Locale

class ChangeUsernameFragment : BaseChangeFragment(R.layout.fragment_change_username) {
    private lateinit var changeUsernameBinding: FragmentChangeUsernameBinding

    private lateinit var moduleNewUsername: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        changeUsernameBinding = FragmentChangeUsernameBinding.inflate(inflater, container, false)
        return changeUsernameBinding.root
    }

    override fun onResume() {
        super.onResume()
        changeUsernameBinding.settingsInputUsername.setText(USER.username)
    }

    override fun change() {
        moduleNewUsername = changeUsernameBinding.settingsInputUsername.text
            .toString()
            .lowercase(Locale.getDefault())

        if (moduleNewUsername.isEmpty()) {
            showFragmentToast(getString(R.string.username_is_empty))
        } else {
            REF_DATABASE_ROOT.child(NODE_USERNAMES)
                .addListenerForSingleValueEvent(AppValueEventListener { task ->
                    if (task.hasChild(moduleNewUsername)) {
                        showFragmentToast(getString(R.string.this_user_is_exist))
                    } else {
                        confirmChangeUsername()
                    }
                })
        }
    }

    private fun confirmChangeUsername() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES)
            .child(moduleNewUsername)
            .setValue(CURRENT_UID)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateCurrentUsername()
                }
            }
    }

    private fun updateCurrentUsername() {
        REF_DATABASE_ROOT.child(NODE_USERS)
            .child(CURRENT_UID)
            .child(CHILD_USERNAME)
            .setValue(moduleNewUsername)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showFragmentToast(getString(R.string.toast_details_update))
                    deleteOldUsername()
                } else {
                    showFragmentToast(task.exception?.message.toString())
                }
            }
    }

    private fun deleteOldUsername() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES)
            .child(USER.username)
            .removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.isSuccessful) {
                        showFragmentToast(getString(R.string.toast_details_update))
                        USER.username = moduleNewUsername
                        parentFragmentManager.popBackStack()
                    } else {
                        showFragmentToast(task.exception?.message.toString())
                    }
                }
            }
    }
}