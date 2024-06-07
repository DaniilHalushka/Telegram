package com.daniil.halushka.telegram.util

import com.daniil.halushka.telegram.database.AUTH
import com.google.firebase.auth.PhoneAuthProvider

fun signInWithCredential(id: String, code: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
    val credential = PhoneAuthProvider.getCredential(id, code)
    AUTH.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onFailure(task.exception?.message.toString())
            }
        }
}
