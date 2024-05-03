package com.daniil.halushka.telegram.util

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

const val READ_CONTACTS = Manifest.permission.READ_CONTACTS
const val RECORD_AUDIO = Manifest.permission.RECORD_AUDIO
const val REQUEST_CODE = 100

fun checkPermission(permission: String): Boolean {
    return if (Build.VERSION.SDK_INT >= 23
        && ContextCompat.checkSelfPermission(APP_ACTIVITY, permission) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(APP_ACTIVITY, arrayOf(permission), REQUEST_CODE)
        false
    } else true
}