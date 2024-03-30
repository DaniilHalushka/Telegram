package com.daniil.halushka.telegram.data.models

data class User(
    val id: String = "",
    var username: String = "",
    var fullname: String = "",
    var phone: String = "",
    var information: String = "",
    var status: String = "",
    var photoUrl: String = "",
)