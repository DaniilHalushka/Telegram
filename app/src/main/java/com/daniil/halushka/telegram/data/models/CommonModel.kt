package com.daniil.halushka.telegram.data.models

data class CommonModel(
    val id: String = "",
    var username: String = "",
    var fullname: String = "",
    var phone: String = "",
    var information: String = "",
    var state: String = "",
    var photoURL: String = "empty",

    var text: String = "",
    var type: String = "",
    var from: String = "",
    var timeStamp: String = ""
)