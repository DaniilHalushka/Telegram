package com.daniil.halushka.telegram.ui.screens.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.databinding.FragmentChangeUsernameBinding
import com.daniil.halushka.telegram.ui.screens.activities.main.MainActivity
import com.daniil.halushka.telegram.ui.screens.fragments.BaseFragment
import com.daniil.halushka.telegram.util.AppValueEventListener
import com.daniil.halushka.telegram.util.CHILD_USERNAME
import com.daniil.halushka.telegram.util.NODE_USERNAMES
import com.daniil.halushka.telegram.util.NODE_USERS
import com.daniil.halushka.telegram.util.REF_DATABASE_ROOT
import com.daniil.halushka.telegram.util.UID
import com.daniil.halushka.telegram.util.USER
import com.daniil.halushka.telegram.util.showToast
import java.util.Locale

class ChangeUsernameFragment : BaseFragment(R.layout.fragment_change_username) {
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
        setHasOptionsMenu(true)
        changeUsernameBinding.settingsInputUsername.setText(USER.username)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as MainActivity).menuInflater.inflate(R.menu.settings_confirm_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_confirm_change -> changeUsername()
        }
        return true
    }

    private fun changeUsername() {
        moduleNewUsername = changeUsernameBinding.settingsInputUsername.text
            .toString()
            .lowercase(Locale.getDefault())

        if (moduleNewUsername.isEmpty()) {
            showToast(getString(R.string.username_is_empty))
        } else {
            REF_DATABASE_ROOT.child(NODE_USERNAMES)
                .addListenerForSingleValueEvent(AppValueEventListener { task ->
                    if (task.hasChild(moduleNewUsername)) {
                        showToast(getString(R.string.this_user_is_exist))
                    } else {
                        confirmChangeUsername()
                    }
                })
        }
    }

    private fun confirmChangeUsername() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES)
            .child(moduleNewUsername)
            .setValue(UID)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateCurrentUsername()
                }
            }
    }

    private fun updateCurrentUsername() {
        REF_DATABASE_ROOT.child(NODE_USERS)
            .child(UID)
            .child(CHILD_USERNAME)
            .setValue(moduleNewUsername)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast(getString(R.string.toast_details_update))
                    deleteOldUsername()
                } else {
                    showToast(task.exception?.message.toString())
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
                        showToast(getString(R.string.toast_details_update))
                        parentFragmentManager.popBackStack() // *TODO* проверить возврат, может тут поломаться
                        USER.username = moduleNewUsername
                    } else {
                        showToast(task.exception?.message.toString())
                    }
                }
            }
    }
}