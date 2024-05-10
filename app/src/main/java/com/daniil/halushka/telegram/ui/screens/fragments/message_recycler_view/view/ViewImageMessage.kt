package com.daniil.halushka.telegram.ui.screens.fragments.message_recycler_view.view

class ViewImageMessage(
    override val id: String,
    override val from: String,
    override val timeStamp: String,
    override val fileUrl: String,
    override val text: String = ""
) : MessageView {
    override fun getTypeView(): Int {
        return MessageView.MESSAGE_IMAGE
    }
}