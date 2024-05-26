package com.daniil.halushka.telegram.ui.screens.message_recycler_view.view_holders

import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.daniil.halushka.telegram.database.CURRENT_UID
import com.daniil.halushka.telegram.database.getFileFromStorage
import com.daniil.halushka.telegram.databinding.MessageItemFileBinding
import com.daniil.halushka.telegram.ui.screens.message_recycler_view.view.MessageView
import com.daniil.halushka.telegram.util.WRITE_FILES
import com.daniil.halushka.telegram.util.asTime
import com.daniil.halushka.telegram.util.checkPermission
import com.daniil.halushka.telegram.util.showToast
import java.io.File

class HolderFileMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {
    private val itemBinding = MessageItemFileBinding.bind(view)

    private val blockReceivedFileMessage: ConstraintLayout = itemBinding.blockReceivedFileMessage
    private val blockUserFileMessage: ConstraintLayout = itemBinding.blockUserFileMessage
    private val chatUserFileMessageTime: TextView = itemBinding.chatUserFileMessageTime
    private val chatReceivedFileMessageTime: TextView = itemBinding.chatReceivedFileMessageTime

    private val chatUserFilename: TextView = itemBinding.chatUserFilename
    private val chatUserButtonDownload: ImageView = itemBinding.chatUserButtonFileDownload
    private val chatUserProgressBar: ProgressBar = itemBinding.chatUserProgressBar

    private val chatReceivedFilename: TextView = itemBinding.chatReceivedFilename
    private val chatReceivedButtonDownload: ImageView = itemBinding.chatReceivedButtonFileDownload
    private val chatReceivedProgressBar: ProgressBar = itemBinding.chatReceivedProgressBar

    override fun onAttach(view: MessageView) {
        if (view.from == CURRENT_UID) chatUserButtonDownload.setOnClickListener {
            clickToButtonFile(view)
        }
        else chatReceivedButtonDownload.setOnClickListener { clickToButtonFile(view) }
    }

    private fun clickToButtonFile(view: MessageView) {
        if (view.from == CURRENT_UID) {
            chatUserButtonDownload.visibility = View.INVISIBLE
            chatUserProgressBar.visibility = View.VISIBLE
        } else {
            chatReceivedButtonDownload.visibility = View.INVISIBLE
            chatReceivedProgressBar.visibility = View.VISIBLE
        }

        val file =
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                view.text
            )

        try {
            if (checkPermission(WRITE_FILES)) {
                file.createNewFile()
                getFileFromStorage(file, view.fileUrl) {
                    if (view.from == CURRENT_UID) {
                        chatUserButtonDownload.visibility = View.VISIBLE
                        chatUserProgressBar.visibility = View.INVISIBLE
                    } else {
                        chatReceivedButtonDownload.visibility = View.VISIBLE
                        chatReceivedProgressBar.visibility = View.INVISIBLE
                    }
                }
            }
        } catch (exception: Exception) {
            showToast(exception.message.toString())
        }
    }

    override fun onDetach() {
        chatUserButtonDownload.setOnClickListener(null)
        chatReceivedButtonDownload.setOnClickListener(null)
    }

    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blockReceivedFileMessage.visibility = View.GONE
            blockUserFileMessage.visibility = View.VISIBLE
            chatUserFileMessageTime.text =
                view.timeStamp.asTime()
            chatUserFilename.text = view.text
        } else {
            blockReceivedFileMessage.visibility = View.VISIBLE
            blockUserFileMessage.visibility = View.GONE
            chatReceivedFileMessageTime.text =
                view.timeStamp.asTime()
            chatReceivedFilename.text = view.text
        }
    }
}