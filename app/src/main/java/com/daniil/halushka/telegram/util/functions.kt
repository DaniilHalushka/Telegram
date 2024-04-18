package com.daniil.halushka.telegram.util

import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.daniil.halushka.telegram.R
import com.daniil.halushka.telegram.data.models.CommonModel
import com.squareup.picasso.Picasso

fun Fragment.showFragmentToast(message: String) {
    Toast.makeText(this.context, message, Toast.LENGTH_SHORT)
        .show()
}

fun showToast(message: String) {
    Toast.makeText(APP_ACTIVITY, message, Toast.LENGTH_SHORT)
        .show()
}

fun initializeContacts() {
    if (checkPermission(READ_CONTACTS)) {
        val arrayContacts = arrayListOf<CommonModel>()
        val cursor = APP_ACTIVITY.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.let {
            while (it.moveToNext()){
                val fullName = it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                val phone = it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val newModel = CommonModel()
                newModel.fullname = fullName
                newModel.phone = phone.replace(Regex("[\\s-]"), "")
                arrayContacts.add(newModel)
            }
        }
        cursor?.close()
        updatePhonesToDatabase(arrayContacts)
    }
}

fun AppCompatActivity.replaceActivity(activity: AppCompatActivity) {
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
    this.finish()
}

fun AppCompatActivity.replaceFragment(
    fragment: Fragment,
    dataContainer: Int,
    addStack: Boolean = true
) {
    if (addStack) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(
                dataContainer,
                fragment
            )
            .commit()
    } else {
        supportFragmentManager.beginTransaction()
            .replace(
                dataContainer,
                fragment
            )
            .commit()
    }

}

fun Fragment.replaceParentFragment(fragment: Fragment, dataContainer: Int) {
    this.parentFragmentManager.beginTransaction()
        .addToBackStack(null)
        .replace(dataContainer, fragment)
        .commit()
}

//*TODO* если метод не понадобится, то потом удалить
//fun Fragment.replaceChildFragment(fragment: Fragment, dataContainer: Int) {
//    this.getChildFragmentManager().beginTransaction() //Поправил тут на getChildFragmentManager
//        .addToBackStack(null)
//        .replace(dataContainer, fragment)
//        .commit()
//}

fun hideKeyboard() {
    val inputManager: InputMethodManager = APP_ACTIVITY
        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(APP_ACTIVITY.window.decorView.windowToken, 0)
}

fun ImageView.downloadAndSetImage(url: String) {
    Picasso.get()
        .load(url)
        .fit()
        .placeholder(R.drawable.default_user)
        .into(this)
}