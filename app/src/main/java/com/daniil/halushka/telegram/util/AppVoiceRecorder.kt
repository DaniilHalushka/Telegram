package com.daniil.halushka.telegram.util

import android.media.MediaRecorder
import java.io.File

class AppVoiceRecorder {

    //TODO deprecated
    private val moduleMediaRecorder = MediaRecorder()
    private lateinit var moduleFile: File
    private lateinit var moduleMessageKey: String
    fun startRecord(messageKey: String) {
        try {
            moduleMessageKey = messageKey
            createFileForRecord()
            prepareMediaRecorder()
            moduleMediaRecorder.start()
        } catch (exception: Exception) {
            showToast(exception.message.toString())
        }
    }

    private fun prepareMediaRecorder() {
        moduleMediaRecorder.apply {
            reset()
            setAudioSource(MediaRecorder.AudioSource.DEFAULT)
            setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
            setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
            setOutputFile(moduleFile.absolutePath)
            prepare()
        }
    }

    private fun createFileForRecord() {
        moduleFile = File(APP_ACTIVITY.filesDir, moduleMessageKey)
        moduleFile.createNewFile()
    }

    fun stopRecord(onSuccess: (file: File, messageKey: String) -> Unit) {
        try {
            moduleMediaRecorder.stop()
            onSuccess(moduleFile, moduleMessageKey)
        } catch (exception: Exception) {
            showToast(exception.message.toString())
            moduleFile.delete()
        }
    }

    fun realiseRecorder() {
        try {
            moduleMediaRecorder.release()
        } catch (exception: Exception) {
            showToast(exception.message.toString())
        }
    }

}