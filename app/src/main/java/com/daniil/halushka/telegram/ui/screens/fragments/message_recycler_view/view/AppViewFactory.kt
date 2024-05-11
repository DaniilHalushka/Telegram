package com.daniil.halushka.telegram.ui.screens.fragments.message_recycler_view.view

import com.daniil.halushka.telegram.data.models.CommonModel
import com.daniil.halushka.telegram.util.TYPE_MESSAGE_IMAGE

class AppViewFactory {
    companion object {
        fun getView(message: CommonModel): MessageView {
            return when (message.type) {
                TYPE_MESSAGE_IMAGE -> ViewImageMessage(
                    message.id,
                    message.from,
                    message.timeStamp.toString(),
                    message.fileUrl
                )

                else -> ViewTextMessage(
                    message.id,
                    message.from,
                    message.timeStamp.toString(),
                    message.fileUrl,
                    message.text
                )
            }
        }
    }
}