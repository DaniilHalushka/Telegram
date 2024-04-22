package com.daniil.halushka.telegram.ui.screens.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.database.CURRENT_UID
import com.daniil.halushka.telegram.database.NODE_USERNAMES
import com.daniil.halushka.telegram.database.REF_DATABASE_ROOT
import com.daniil.halushka.telegram.database.USER
import com.daniil.halushka.telegram.database.updateCurrentUsername
import com.daniil.halushka.telegram.databinding.FragmentChangeUsernameBinding
import com.daniil.halushka.telegram.ui.screens.fragments.BaseChangeFragment
import com.daniil.halushka.telegram.util.AppValueEventListener
import com.daniil.halushka.telegram.util.showToast
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
            .setValue(CURRENT_UID)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateCurrentUsername(moduleNewUsername)
                }
            }
    }
}