package com.daniil.halushka.telegram.util

import com.daniil.halushka.telegram.database.AUTH
import com.daniil.halushka.telegram.database.CHILD_ID
import com.daniil.halushka.telegram.database.CHILD_PHONE
import com.daniil.halushka.telegram.database.NODE_PHONES
import com.daniil.halushka.telegram.database.NODE_USERS
import com.daniil.halushka.telegram.database.REF_DATABASE_ROOT

fun saveUserToDatabase(phoneNumber: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
    val userId = AUTH.currentUser?.uid.toString()
    val dataMap = mutableMapOf<String, Any>()
    dataMap[CHILD_ID] = userId
    dataMap[CHILD_PHONE] = phoneNumber

    REF_DATABASE_ROOT.child(NODE_PHONES).child(phoneNumber).setValue(userId)
        .addOnFailureListener { onFailure(it.message.toString()) }
        .addOnSuccessListener {
            REF_DATABASE_ROOT.child(NODE_USERS).child(userId)
                .updateChildren(dataMap)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { onFailure(it.message.toString()) }
        }
}
