package com.daniil.halushka.telegram.data.models

data class UserModel(
    val id: String = "",
    var username: String = "",
    var fullname: String = "",
    var phone: String = "",
    var information: String = "",
    var state: String = "",
    var photoURL: String = "empty",
)