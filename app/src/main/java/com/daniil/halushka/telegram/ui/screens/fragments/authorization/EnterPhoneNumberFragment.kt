package com.daniil.halushka.telegram.ui.screens.fragments.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.databinding.FragmentEnterPhoneNumberBinding

class EnterPhoneNumberFragment : Fragment(R.layout.fragment_enter_phone_number) {

    private var _phoneBinding: FragmentEnterPhoneNumberBinding? = null
    private val phoneBinding get() = _phoneBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _phoneBinding = FragmentEnterPhoneNumberBinding.inflate(inflater, container, false)

        return phoneBinding.root
    }

    override fun onStart() {
        super.onStart()
        phoneBinding.registerNextButton.setOnClickListener {
            sendCode()
        }
    }

    private fun sendCode() {
        if (phoneBinding.registerInputPhoneNumber.text.toString().isEmpty()) {
            Toast.makeText(activity, getString(R.string.enter_phone_number), Toast.LENGTH_SHORT)
                .show()
        } else {
            parentFragmentManager.beginTransaction()
                .replace(R.id.authorizationDataContainer, EnterCodeFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _phoneBinding = null
    }
}