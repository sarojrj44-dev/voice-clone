package com.example.voicecloneapp.audio

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AudioRecorder(private val outputFile: File) {
    private var audioRecord: AudioRecord? = null
    private var isRecording = false
    private var recordingJob: Job? = null
    
    private val sampleRate = 44100
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
    
    fun startRecording(scope: CoroutineScope) {
        if (isRecording) return
        
        try {
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                channelConfig,
                audioFormat,
                bufferSize
            )
            
            audioRecord?.startRecording()
            isRecording = true
            
            recordingJob = scope.launch(Dispatchers.IO) {
                recordAudio()
            }
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Error starting recording", e)
        }
    }
    
    fun stopRecording() {
        if (!isRecording) return
        
        isRecording = false
        recordingJob?.cancel()
        
        audioRecord?.apply {
            stop()
            release()
        }
        audioRecord = null
    }
    
    private suspend fun recordAudio() {
        val buffer = ByteArray(bufferSize)
        val outputStream = FileOutputStream(outputFile)
        
        try {
            while (isRecording) {
                val read = audioRecord?.read(buffer, 0, bufferSize) ?: 0
                if (read > 0) {
                    withContext(Dispatchers.IO) {
                        outputStream.write(buffer, 0, read)
                    }
                }
            }
        } catch (e: IOException) {
            Log.e("AudioRecorder", "Error writing audio data", e)
        } finally {
            withContext(Dispatchers.IO) {
                try {
                    outputStream.close()
                } catch (e: IOException) {
                    Log.e("AudioRecorder", "Error closing output stream", e)
                }
            }
        }
    }
    
    fun isRecording(): Boolean = isRecording
}