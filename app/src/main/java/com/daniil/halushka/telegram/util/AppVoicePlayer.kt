package com.daniil.halushka.telegram.util

import android.media.MediaPlayer
import com.daniil.halushka.telegram.database.getFileFromStorage
import java.io.File

class AppVoicePlayer {
    private lateinit var moduleMediaPlayer: MediaPlayer
    private lateinit var moduleFile: File

    fun initializeMediaPlayer() {
        moduleMediaPlayer = MediaPlayer()
    }

    fun play(messageKey: String, fileUrl: String, function: () -> Unit) {
        moduleFile = File(APP_ACTIVITY.filesDir, messageKey)
        if (moduleFile.exists() && moduleFile.length() > 0 && moduleFile.isFile) {
            startPlay {
                function()
            }
        } else {
            moduleFile.createNewFile()
            getFileFromStorage(moduleFile, fileUrl) {
                startPlay {
                    function()
                }
            }
        }
    }

    private fun startPlay(function: () -> Unit) {
        try {
            moduleMediaPlayer.setDataSource(moduleFile.absolutePath)
            moduleMediaPlayer.prepare()
            moduleMediaPlayer.start()
            moduleMediaPlayer.setOnCompletionListener {
                stopPlay {
                    function()
                }
            }
        } catch (exception: Exception) {
            showToast(exception.message.toString())
        }
    }

    fun stopPlay(function: () -> Unit) {
        try {
            moduleMediaPlayer.stop()
            moduleMediaPlayer.reset()
            function()
        } catch (exception: Exception) {
            showToast(exception.message.toString())
            function()
        }
    }

    fun realise() {
        moduleMediaPlayer.release()
    }
}