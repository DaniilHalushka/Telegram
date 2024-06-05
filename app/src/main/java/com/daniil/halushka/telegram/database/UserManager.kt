package com.daniil.halushka.telegram.database

import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.data.models.UserModel
import com.daniil.halushka.telegram.util.APP_ACTIVITY
import com.daniil.halushka.telegram.util.AppValueEventListener
import com.daniil.halushka.telegram.util.showToast

inline fun initializeUser(crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
        .addListenerForSingleValueEvent(
            AppValueEventListener {
                USER = it.getValue(UserModel::class.java) ?: UserModel()

                if (USER.username.isEmpty()) USER.username = CHILD_USERNAME
                function()
            }
        )
}

fun setNameToDatabase(fullName: String) {
    REF_DATABASE_ROOT.child(NODE_USERS)
        .child(CURRENT_UID)
        .child(CHILD_FULLNAME)
        .setValue(fullName)
        .addOnSuccessListener {
            showToast(APP_ACTIVITY.getString(R.string.toast_details_update))
            USER.fullname = fullName
            APP_ACTIVITY.moduleAppDrawer.updateHeader()
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun updateCurrentUsername(newUsername: String) {
    REF_DATABASE_ROOT.child(NODE_USERS)
        .child(CURRENT_UID)
        .child(CHILD_USERNAME)
        .setValue(newUsername)
        .addOnSuccessListener {
            showToast(APP_ACTIVITY.getString(R.string.toast_details_update))
            deleteOldUsername(newUsername)
        }
        .addOnFailureListener { showToast(it.message.toString()) }
}

private fun deleteOldUsername(newUsername: String) {
    REF_DATABASE_ROOT.child(NODE_USERNAMES)
        .child(USER.username)
        .removeValue()
        .addOnSuccessListener {
            showToast(APP_ACTIVITY.getString(R.string.toast_details_update))
            USER.username = newUsername
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun setInfoToDatabase(newInformation: String) {
    REF_DATABASE_ROOT.child(NODE_USERS)
        .child(CURRENT_UID)
        .child(CHILD_INFORMATION)
        .setValue(newInformation)
        .addOnSuccessListener {
            showToast(APP_ACTIVITY.getString(R.string.toast_details_update))
            USER.information = newInformation
            APP_ACTIVITY.supportFragmentManager.popBackStack()
        }
        .addOnFailureListener { showToast(it.message.toString()) }
}