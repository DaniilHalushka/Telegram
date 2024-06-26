package com.daniil.halushka.telegram.util

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class AppChildEventListener(val onSuccess: (DataSnapshot) -> Unit) : ChildEventListener {
    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        onSuccess(snapshot)
    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        onSuccess(snapshot)
    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        onSuccess(snapshot)
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        onSuccess(snapshot)
    }

    override fun onCancelled(error: DatabaseError) {
        showToast(error.message)
    }
}