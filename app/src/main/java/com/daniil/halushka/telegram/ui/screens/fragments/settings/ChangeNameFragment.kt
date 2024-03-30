package com.daniil.halushka.telegram.ui.screens.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.databinding.FragmentChangeNameBinding
import com.daniil.halushka.telegram.ui.screens.activities.main.MainActivity
import com.daniil.halushka.telegram.util.CHILD_FULLNAME
import com.daniil.halushka.telegram.util.NODE_USERS
import com.daniil.halushka.telegram.util.REF_DATABASE_ROOT
import com.daniil.halushka.telegram.util.UID
import com.daniil.halushka.telegram.util.USER
import com.daniil.halushka.telegram.util.showToast

class ChangeNameFragment : Fragment(R.layout.fragment_change_name) {

    private var _changeNameBinding: FragmentChangeNameBinding? = null
    private val changeNameBinding get() = _changeNameBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _changeNameBinding = FragmentChangeNameBinding.inflate(inflater, container, false)
        return changeNameBinding.root
    }


    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)

        val fullNameList = USER.fullname.split(" ")
        if (fullNameList.size > 1) {                                    //*TODO* Подумать, как написать этот if лучше
            changeNameBinding.settingsInputName.setText(fullNameList[0])
            changeNameBinding.settingsInputSurname.setText(fullNameList[1])
        } else {
            changeNameBinding.settingsInputName.setText("")
            changeNameBinding.settingsInputSurname.setText("")
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as MainActivity).menuInflater.inflate(R.menu.settings_confirm_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_confirm_change -> changeName()
        }

        return true
    }

    private fun changeName() {
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