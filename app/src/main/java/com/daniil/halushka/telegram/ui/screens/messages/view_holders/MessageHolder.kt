package com.daniil.halushka.telegram.ui.screens.messages.view_holders

import com.daniil.halushka.telegram.ui.screens.messages.view.MessageView

interface MessageHolder {
    fun drawMessage(view: MessageView)
    fun onAttach(view: MessageView)
    fun onDetach()
}