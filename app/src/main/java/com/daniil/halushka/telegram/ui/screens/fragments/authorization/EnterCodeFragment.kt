package com.daniil.halushka.telegram.ui.screens.fragments.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.databinding.FragmentEnterCodeBinding
import com.daniil.halushka.telegram.ui.screens.activities.authorization.AuthorizationActivity
import com.daniil.halushka.telegram.ui.screens.activities.main.MainActivity
import com.daniil.halushka.telegram.util.AUTH
import com.daniil.halushka.telegram.util.AppTextWatcher
import com.daniil.halushka.telegram.util.CHILD_ID
import com.daniil.halushka.telegram.util.CHILD_PHONE
import com.daniil.halushka.telegram.util.CHILD_USERNAME
import com.daniil.halushka.telegram.util.NODE_USERS
import com.daniil.halushka.telegram.util.REF_DATABASE_ROOT
import com.daniil.halushka.telegram.util.replaceActivity
import com.daniil.halushka.telegram.util.showFragmentToast
import com.google.firebase.auth.PhoneAuthProvider

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
        (activity as AuthorizationActivity).title = phoneNumber
        codeBinding.registerInputCode.addTextChangedListener(AppTextWatcher {
            val string = codeBinding.registerInputCode.text.toString()
            if (string.length == 6) {
                enterVerificationCode()
            }
        })
    }

    private fun enterVerificationCode() {
        val code = codeBinding.registerInputCode.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = AUTH.currentUser?.uid.toString()
                    val dataMap = mutableMapOf<String, Any>()
                    dataMap[CHILD_ID] = userId
                    dataMap[CHILD_PHONE] = phoneNumber
                    dataMap[CHILD_USERNAME] = userId

                    REF_DATABASE_ROOT.child(NODE_USERS).child(userId)
                        .updateChildren(dataMap)
                        .addOnCompleteListener { secondTask ->
                            if (secondTask.isSuccessful) {
                                showFragmentToast(getString(R.string.auth_complete))
                                (activity as AuthorizationActivity)
                                    .replaceActivity(MainActivity())
                            } else showFragmentToast(secondTask.exception?.message.toString())
                        }
                } else showFragmentToast(task.exception?.message.toString())
            }
    }
}