package com.daniil.halushka.telegram.util

import android.media.MediaPlayer
import java.io.File

class AppVoicePlayer {
    private lateinit var moduleMediaPlayer: MediaPlayer
    private lateinit var moduleFile: File

    fun play(messageKey: String, fileUrl: String, function: () -> Unit) {
        moduleFile = File(APP_ACTIVITY.filesDir, messageKey)
        if (moduleFile.exists() && moduleFile.length() > 0 && moduleFile.isFile) {
            startPlay {
                function()
            }
        }
    }

    private fun startPlay(function: () -> Unit) {
        try {
            moduleMediaPlayer.setDataSource(moduleFile.absolutePath)
            moduleMediaPlayer.prepare()
            moduleMediaPlayer.start()
            moduleMediaPlayer.setOnCompletionListener {
                stop {
                    function()
                }
            }
        } catch (exception: Exception) {
            showToast(exception.message.toString())
        }
    }

    fun stop(function: () -> Unit) {

    }

    fun realise() {

    }
}