package com.daniil.halushka.telegram.database

import com.daniil.halushka.telegram.data.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

fun initializeFirebase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT =
        FirebaseDatabase.getInstance("https://telegram-665f2-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference()
    USER = UserModel()
    CURRENT_UID = AUTH.currentUser?.uid.toString()
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().getReference()
}