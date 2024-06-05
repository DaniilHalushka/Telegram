package com.daniil.halushka.telegram.database

import com.daniil.halushka.telegram.data.models.CommonModel
import com.daniil.halushka.telegram.util.AppValueEventListener
import com.daniil.halushka.telegram.util.showToast

fun updatePhonesToDatabase(arrayContacts: ArrayList<CommonModel>) {
    if (AUTH.currentUser != null) {
        REF_DATABASE_ROOT.child(NODE_PHONES)
            .addListenerForSingleValueEvent(AppValueEventListener { it ->
                it.children.forEach { dataSnapshot ->
                    arrayContacts.forEach { contact ->
                        if (dataSnapshot.key == contact.phone) {
                            REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS)
                                .child(CURRENT_UID)
                                .child(dataSnapshot.value.toString())
                                .child(CHILD_ID)
                                .setValue(dataSnapshot.value.toString())
                                .addOnFailureListener { showToast(it.message.toString()) }

                            REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS)
                                .child(CURRENT_UID)
                                .child(dataSnapshot.value.toString())
                                .child(CHILD_FULLNAME)
                                .setValue(contact.fullname)
                                .addOnFailureListener { showToast(it.message.toString()) }
                        }
                    }
                }
            })
    }
}