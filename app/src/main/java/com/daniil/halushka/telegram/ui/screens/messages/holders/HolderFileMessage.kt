package com.daniil.halushka.telegram.ui.screens.messages.holders

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
import com.daniil.halushka.telegram.ui.screens.messages.view.MessageView
import com.daniil.halushka.telegram.util.WRITE_FILES
import com.daniil.halushka.telegram.util.asTime
import com.daniil.halushka.telegram.util.checkPermission
import com.daniil.halushka.telegram.util.showToast
import java.io.File

class HolderFileMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {
    private val binding = MessageItemFileBinding.bind(view)

    private val receivedFileMessageBlock: ConstraintLayout = binding.blockReceivedFileMessage
    private val userFileMessageBlock: ConstraintLayout = binding.blockUserFileMessage
    private val userFileMessageTime: TextView = binding.chatUserFileMessageTime
    private val receivedFileMessageTime: TextView = binding.chatReceivedFileMessageTime

    private val userFilename: TextView = binding.chatUserFilename
    private val userDownloadButton: ImageView = binding.chatUserButtonFileDownload
    private val userProgressBar: ProgressBar = binding.chatUserProgressBar

    private val receivedFilename: TextView = binding.chatReceivedFilename
    private val receivedDownloadButton: ImageView = binding.chatReceivedButtonFileDownload
    private val receivedProgressBar: ProgressBar = binding.chatReceivedProgressBar

    override fun onAttach(view: MessageView) {
        val downloadButton = if (view.from == CURRENT_UID) userDownloadButton else receivedDownloadButton
        downloadButton.setOnClickListener { handleFileDownload(view) }
    }

    private fun handleFileDownload(view: MessageView) {
        val (downloadButton, progressBar) = if (view.from == CURRENT_UID) {
            userDownloadButton to userProgressBar
        } else {
            receivedDownloadButton to receivedProgressBar
        }

        downloadButton.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE

        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            view.text
        )

        try {
            if (checkPermission(WRITE_FILES)) {
                file.createNewFile()
                getFileFromStorage(file, view.fileUrl) {
                    downloadButton.visibility = View.VISIBLE
                    progressBar.visibility = View.INVISIBLE
                }
            }
        } catch (exception: Exception) {
            showToast(exception.message.toString())
        }
    }

    override fun onDetach() {
        userDownloadButton.setOnClickListener(null)
        receivedDownloadButton.setOnClickListener(null)
    }

    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            showUserMessage(view)
        } else {
            showReceivedMessage(view)
        }
    }

    private fun showUserMessage(view: MessageView) {
        receivedFileMessageBlock.visibility = View.GONE
        userFileMessageBlock.visibility = View.VISIBLE
        userFileMessageTime.text = view.timeStamp.asTime()
        userFilename.text = view.text
    }

    private fun showReceivedMessage(view: MessageView) {
        receivedFileMessageBlock.visibility = View.VISIBLE
        userFileMessageBlock.visibility = View.GONE
        receivedFileMessageTime.text = view.timeStamp.asTime()
        receivedFilename.text = view.text
    }
}
