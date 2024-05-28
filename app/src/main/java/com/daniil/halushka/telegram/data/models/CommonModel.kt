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
    var timeStamp: Any = "",
    var fileUrl: String = "empty",

    var lastMessage: String = ""
) {
    override fun equals(other: Any?): Boolean {
        return (other as CommonModel).id == id
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + fullname.hashCode()
        result = 31 * result + phone.hashCode()
        result = 31 * result + information.hashCode()
        result = 31 * result + state.hashCode()
        result = 31 * result + photoURL.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + from.hashCode()
        result = 31 * result + timeStamp.hashCode()
        return result
    }
}