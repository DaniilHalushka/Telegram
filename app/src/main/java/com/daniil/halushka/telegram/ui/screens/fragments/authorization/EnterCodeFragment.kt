package com.daniil.halushka.telegram.ui.screens.fragments.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.databinding.FragmentEnterCodeBinding
import com.daniil.halushka.telegram.util.APP_ACTIVITY
import com.daniil.halushka.telegram.util.AppTextWatcher
import com.daniil.halushka.telegram.util.restartActivity
import com.daniil.halushka.telegram.util.saveUserToDatabase
import com.daniil.halushka.telegram.util.showToast
import com.daniil.halushka.telegram.util.signInWithCredential

class EnterCodeFragment(
    private val phoneNumber: String,
    val id: String
) : Fragment(R.layout.fragment_enter_code) {
    private lateinit var codeBinding: FragmentEnterCodeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        codeBinding = FragmentEnterCodeBinding.inflate(inflater, container, false)
        return codeBinding.root
    }

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.title = phoneNumber
        codeBinding.registerInputCode.addTextChangedListener(AppTextWatcher {
            val string = codeBinding.registerInputCode.text.toString()
            if (string.length == 6) {
                enterVerificationCode()
            }
        })
    }

    private fun enterVerificationCode() {
        val code = codeBinding.registerInputCode.text.toString()
        signInWithCredential(id, code, ::onSignInSuccess, ::onSignInFailure)
    }

    private fun onSignInSuccess() {
        saveUserToDatabase(phoneNumber, ::onDatabaseSuccess, ::onDatabaseFailure)
    }

    private fun onSignInFailure(errorMessage: String) {
        showToast(errorMessage)
    }

    private fun onDatabaseSuccess() {
        showToast(getString(R.string.auth_complete))
        restartActivity()
    }

    private fun onDatabaseFailure(errorMessage: String) {
        showToast(errorMessage)
    }
}
