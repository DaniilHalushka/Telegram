package com.daniil.halushka.telegram.ui.screens.fragments.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.databinding.FragmentEnterPhoneNumberBinding
import com.daniil.halushka.telegram.ui.screens.activities.authorization.AuthorizationActivity
import com.daniil.halushka.telegram.ui.screens.activities.main.MainActivity
import com.daniil.halushka.telegram.util.AUTH
import com.daniil.halushka.telegram.util.replaceActivity
import com.daniil.halushka.telegram.util.replaceParentFragment
import com.daniil.halushka.telegram.util.showToast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class EnterPhoneNumberFragment : Fragment(R.layout.fragment_enter_phone_number) {

    private var _phoneBinding: FragmentEnterPhoneNumberBinding? = null
    private val phoneBinding get() = _phoneBinding!!

    private lateinit var modulePhoneNumber: String
    private lateinit var moduleCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks

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
        moduleCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                AUTH.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showToast(getString(R.string.auth_complete))
                            (activity as AuthorizationActivity)
                                .replaceActivity(MainActivity())
                        } else {
                            showToast(task.exception?.message.toString())
                        }
                    }
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                replaceParentFragment(EnterCodeFragment(modulePhoneNumber, id))
            }

            override fun onVerificationFailed(firebaseException: FirebaseException) {
                showToast(firebaseException.message.toString())
            }
        }
        phoneBinding.registerNextButton.setOnClickListener {
            sendCode()
        }
    }

    private fun sendCode() {
        if (phoneBinding.registerInputPhoneNumber.text.toString().isEmpty()) {
            showToast(getString(R.string.enter_phone_number))
        } else {
            //replaceParentFragment(EnterCodeFragment())
            authUser()
        }
    }

    private fun authUser() {
        modulePhoneNumber = phoneBinding.registerInputPhoneNumber.text.toString()
        val options = PhoneAuthOptions.newBuilder(AUTH)
            .setPhoneNumber(modulePhoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity as AuthorizationActivity)
            .setCallbacks(moduleCallback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _phoneBinding = null
    }
}