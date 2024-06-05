package com.daniil.halushka.telegram.database

import android.net.Uri
import com.daniil.halushka.telegram.util.showToast
import com.google.firebase.storage.StorageReference
import java.io.File

inline fun putFileToStorage(uri: Uri, path: StorageReference, crossinline function: () -> Unit) {
    path.putFile(uri)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun getUrlFromStorage(path: StorageReference, crossinline function: (url: String) -> Unit) {
    path.downloadUrl
        .addOnSuccessListener { function(it.toString()) }
        .addOnFailureListener { showToast(it.message.toString()) }
}

inline fun putUrlToDB(url: String, crossinline function: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
        .child(CHILD_PHOTOURL).setValue(url)
        .addOnSuccessListener { function() }
        .addOnFailureListener { showToast(it.message.toString()) }
}

fun uploadFileToStorage(
    uri: Uri,
    messageKey: String,
    receivedID: String,
    typeMessage: String,
    filename: String = ""
) {
    val path = REF_STORAGE_ROOT.child(FOLDER_FILES)
        .child(messageKey)
    putFileToStorage(uri, path) {
        getUrlFromStorage(path) { url ->
            sendMessageAsFile(receivedID, url, messageKey, typeMessage, filename)
        }
    }
}

fun getFileFromStorage(moduleFile: File, fileUrl: String, function: () -> Unit) {
    val path = REF_STORAGE_ROOT.storage.getReferenceFromUrl(fileUrl)
    path.getFile(moduleFile)
        .addOnSuccessListener {
            function()
        }
        .addOnFailureListener {
            showToast(it.message.toString())
        }
}