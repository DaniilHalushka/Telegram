package com.daniil.halushka.telegram.util

import com.daniil.halushka.telegram.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var UID: String
lateinit var USER: User

const val NODE_USERS = "users"
const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USERNAME = "username"
const val CHILD_FULLNAME = "fullname"

fun initializeFirebase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase
        .getInstance("https://telegram-665f2-default-rtdb.europe-west1.firebasedatabase.app/")
        .getReference()
    USER = User()
    UID = AUTH.currentUser?.uid.toString()
}