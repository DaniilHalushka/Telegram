package com.daniil.halushka.telegram.ui.screens.fragments.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.databinding.FragmentEnterCodeBinding
import com.daniil.halushka.telegram.util.AppTextWatcher

class EnterCodeFragment : Fragment(R.layout.fragment_enter_code) {
    private var _codeBinding: FragmentEnterCodeBinding? = null
    private val codeBinding get() = _codeBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _codeBinding = FragmentEnterCodeBinding.inflate(inflater, container, false)
        return codeBinding.root
    }

    override fun onStart() {
        super.onStart()
        codeBinding.registerInputCode.addTextChangedListener(AppTextWatcher {
            val string = codeBinding.registerInputCode.text.toString()
            if (string.length == 6) {
                verificationCode()
            }
        })
    }

    private fun verificationCode() {
        Toast.makeText(activity, "OK", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _codeBinding = null
    }
}