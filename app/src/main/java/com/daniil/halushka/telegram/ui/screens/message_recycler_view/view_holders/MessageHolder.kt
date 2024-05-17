package com.daniil.halushka.telegram.ui.screens.message_recycler_view.view_holders

import com.daniil.halushka.telegram.ui.screens.message_recycler_view.view.MessageView

interface MessageHolder {
    fun drawMessage(view: MessageView)
    fun onAttach(view: MessageView)
    fun onDetach()
}