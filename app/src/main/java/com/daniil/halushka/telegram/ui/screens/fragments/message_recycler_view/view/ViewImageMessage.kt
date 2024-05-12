package com.daniil.halushka.telegram.ui.screens.fragments.message_recycler_view.view

data class ViewImageMessage(
    override val id: String,
    override val from: String,
    override val timeStamp: String,
    override val fileUrl: String,
    override val text: String = ""
) : MessageView {
    override fun getTypeView(): Int {
        return MessageView.MESSAGE_IMAGE
    }

    override fun equals(other: Any?): Boolean {
        return (other as MessageView).id == id
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + from.hashCode()
        result = 31 * result + timeStamp.hashCode()
        result = 31 * result + fileUrl.hashCode()
        result = 31 * result + text.hashCode()
        return result
    }
}