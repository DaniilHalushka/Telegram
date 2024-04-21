package com.daniil.halushka.telegram.util

import com.daniil.halushka.telegram.database.AUTH
import com.daniil.halushka.telegram.database.CHILD_STATE
import com.daniil.halushka.telegram.database.CURRENT_UID
import com.daniil.halushka.telegram.database.NODE_USERS
import com.daniil.halushka.telegram.database.REF_DATABASE_ROOT
import com.daniil.halushka.telegram.database.USER

enum class AppStates(val state: String) {
    ONLINE("online"),
    OFFLINE("last seen recently"),
    TYPING("type message");

    companion object {
        fun updateState(appStates: AppStates) {
            if (AUTH.currentUser != null) {
                REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
                    .child(CHILD_STATE)
                    .setValue(appStates.state)
                    .addOnSuccessListener {
                        USER.state = appStates.state
                    }
                    .addOnFailureListener {
                        showToast(it.message.toString())
                    }
            }
        }
    }
}